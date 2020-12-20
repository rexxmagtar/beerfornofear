package com.example.beerproject.activities.ui.beerCatalogOnline

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.beerproject.R
import com.example.beerproject.activities.ui.beerCatalogOnline.beerInfoList.BeerInfoAdapter
import kotlinx.coroutines.*
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

        var infoes = emptyArray<BeerInfoAdapter.BeerInfo>()

        var adapter = BeerInfoAdapter(infoes, requireContext());

        var listView = root.findViewById<ListView>(R.id.beerList);

        listView.adapter = adapter;

        getBeerInfo(10000) {
            requireActivity().runOnUiThread {
                adapter.infoes = it;
                adapter.notifyDataSetChanged();
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

    public fun getBeerInfo(
        limitCount: Int,
        listener: (result: Array<BeerInfoAdapter.BeerInfo>?) -> Unit
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

            var request = JsonObjectRequest(url, null, Response.Listener {


                GlobalScope.launch {
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

                            photo = BitmapDrawable(resources, map);
                        } else {
                            photo = resources.getDrawable(R.drawable.vodka);
                        }

                        var description = "Category: " + category + " \n" +
                                "ABV: " + abv + " \n\n" +
                                descr + " \n"

                        var beerInfo = BeerInfoAdapter.BeerInfo(name, photo, description);

                        result!!.add(beerInfo)

                        listener.invoke(result.toArray(emptyArray<BeerInfoAdapter.BeerInfo>()));

                    }

                    finished = true;
                }

            }, Response.ErrorListener {

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

            listener.invoke(result.toArray(emptyArray<BeerInfoAdapter.BeerInfo>()));

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

    public fun saveBeerInfo(info: BeerInfoAdapter.BeerInfo) {

        TODO("SAVE BEER INFO IN DATABASE")
    }

}