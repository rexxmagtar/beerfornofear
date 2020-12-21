package com.example.beerproject.activities.ui.beerCatalogOnline

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.beerproject.R
import com.example.beerproject.activities.ui.beerCatalogOnline.beerInfoList.BeerInfoAdapter
import com.example.beerproject.database.DataBase
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min


class BeerCatalogOnlineFragment : Fragment() {

    private lateinit var beerCatalogOnlineViewModel: BeerCatalogOnlineViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)


        requestPermissions(Array(1) { "android.permission.INTERNET" }, 0)

        beerCatalogOnlineViewModel =
            ViewModelProvider(this).get(BeerCatalogOnlineViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_beer_catalog_online, container, false)

        var infoes = ArrayList<BeerInfoAdapter.BeerInfo>()

        var adapter = BeerInfoAdapter(infoes, requireContext());

        var listView = root.findViewById<ListView>(R.id.beerList);

        listView.adapter = adapter;

        getBeerInfo(10000) {

            try {
                if (!isAdded) {
                    return@getBeerInfo
                }
                requireActivity().runOnUiThread {

                    if (!isAdded) {
                        return@runOnUiThread
                    }

                    try {
                        adapter.infoes = it;
                        adapter.notifyDataSetChanged();
                    } catch (ex: java.lang.Exception) {
                        System.out.println("Exception " + ex.message)
                    }

                }
            } catch (ex: java.lang.Exception) {
                System.out.println("Exception " + ex.message)
            }
        };

        listView.setOnItemClickListener { parent, view, position, id ->

            var bInfo = view.findViewById<LinearLayout>(R.id.AdditionalBeerInfo);

            var item = adapter.infoes!![position];

            item.isExpanded = !item.isExpanded

            adapter.notifyDataSetChanged()

        }

        listView.setOnItemLongClickListener { parent, view, position, id ->


            saveBeerInfo(adapter.infoes!![position])

            true
        }

        return root
    }

    var scope: Job? = null;

    public fun getBeerInfo(
        limitCount: Int,
        listener: (result: ArrayList<BeerInfoAdapter.BeerInfo>?) -> Unit
    ) {

        var result = ArrayList<BeerInfoAdapter.BeerInfo>();

        var finished = false;
        System.out.println("thread before: " + Thread.currentThread().name)

        CoroutineScope(Dispatchers.Main).launch {

            System.out.println("thread: " + Thread.currentThread().name)
            var url =
                "https://sandbox-api.brewerydb.com/v2/beers/?p=1&hasLabels=Y&order=updateDate&key=677c7fa8ca52646caefb3ee00f11b267";

//            val params = JSONObject()
//            params.put("x-rapidapi-key", "03f3fe5662msh1e87153aa5db028p1a84d8jsn7d5919c12887")
//            params.put("x-rapidapi-host", "brianiswu-open-brewery-db-v1.p.rapidapi.com")

            var request = JsonObjectRequest(url, null, {


                scope = GlobalScope.launch {
                    System.out.println("Loaded beer.")

                    var beerList = it.getJSONArray("data");

                    System.out.println("Finished to load. Loaded " + beerList.length() + " beers")

                    for (i in 0 until (min(limitCount, beerList.length()))) {

//                    System.out.println("paring: " + i)

                        var name = beerList.getJSONObject(i).getString("name");

                        var category = "unknown";

                        if (beerList.getJSONObject(i).has("style")) {
                            category =
                                beerList.getJSONObject(i).getJSONObject("style")
                                    .getJSONObject("category")
                                    .getString("name");
                        }

                        var abv = "unknown"

                        if (beerList.getJSONObject(i).has("abv")) {
                            abv = beerList.getJSONObject(i).getString("abv");
                        }

                        var descr = "";

                        if (beerList.getJSONObject(i).has("description")) {
                            descr = beerList.getJSONObject(i).getString("description");
                        }

                        var photoUrl = ""

                        if (beerList.getJSONObject(i).has("labels")) {
                            photoUrl =
                                beerList.getJSONObject(i).getJSONObject("labels")
                                    .getString("medium");
                        }


                        var photo: Drawable? = null;

                        if (photoUrl.isNotEmpty()) {
                            var map = getBitmapFromURL(photoUrl)

                            if (isAdded) {
                                try {
                                    photo = BitmapDrawable(resources, map);
                                } catch (ex: Exception) {
                                    System.out.println("Exception: " + ex.message)
                                }
                            } else {
                                return@launch
                            }
                        } else {
                            photo = resources.getDrawable(R.drawable.vodka);
                        }

                        var description = "Category: " + category + " \n" +
                                "ABV: " + abv + " \n\n" +
                                descr + " \n"

                        var beerInfo = BeerInfoAdapter.BeerInfo(name, photo, description);

                        result!!.add(beerInfo)

                        listener.invoke(result);

                    }

                    finished = true;
                }

            }, {

                Log.println(Log.ERROR, "SOME TAG", "Beer load error!!!  " + it.message);

                finished = true;

            })

            val queue = Volley.newRequestQueue(context)

            queue.add(request)

            while (!finished) {
//Waiting
                delay(1)
                /* println("Waiting")*/
            }

            listener.invoke(result);

        }

    }

    suspend fun getBitmapFromURL(src: String?): Bitmap? {

        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    }

    override fun onDetach() {
        super.onDetach()
        try {
            System.out.println("trying to cancel job")
            scope?.cancel("fragment was destroyed")
            System.out.println("canceled job")
        } catch (ex: java.lang.Exception) {
            System.out.println(ex.message)
        }
    }

    public fun saveBeerInfo(info: BeerInfoAdapter.BeerInfo) {

        var database = DataBase(context)

        var querryStr =
            "INSERT INTO " + database.TABLE_BEER + "(name,photo,description) VALUES(?, ?, ?) "

        var querry = database.writableDatabase.compileStatement(querryStr);

        if (database.readableDatabase
                .rawQuery(
                    "SELECT * FROM BEER " +
                            "WHERE name = " + "'" + info.title + "'" + " AND description = " + "'" + info.description + "'",
                    null
                ).moveToNext()
        ) {
            Toast.makeText(context, "Beer info already added to favorites", Toast.LENGTH_SHORT)
                .show()

            return
        }

        querry.bindString(1, info.title);
        querry.bindBlob(2, getDrawableBytes(info.photo!!))
        querry.bindString(3, info.description);

        if (querry.executeInsert() < 0) {
            System.out.println("Failed to insert")

            Toast.makeText(context, "Failed to add beer info", Toast.LENGTH_SHORT).show()

        } else {
            System.out.println("Inserted succesfully")

            Toast.makeText(context, "Beer info added to favorites", Toast.LENGTH_SHORT).show()
        }

    }

    public fun getDrawableBytes(d: Drawable): ByteArray {

        val bitmap = (d as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapdata: ByteArray = stream.toByteArray()

        return bitmapdata
    }

}