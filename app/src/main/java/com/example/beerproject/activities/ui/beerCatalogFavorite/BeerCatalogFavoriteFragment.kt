package com.example.beerproject.activities.ui.beerCatalogFavorite

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beerproject.R
import com.example.beerproject.activities.ui.beerCatalogOnline.beerInfoList.BeerInfoAdapter
import com.example.beerproject.database.DataBase
import java.io.ByteArrayOutputStream
import java.util.ArrayList


class BeerCatalogFavoriteFragment : Fragment() {

    private lateinit var beerCatalogFavoriteViewModel: BeerCatalogFavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beerCatalogFavoriteViewModel =
            ViewModelProvider(this).get(BeerCatalogFavoriteViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_beer_catalog_favorite, container, false)

        var infoes = loadBeerInfo()

        var adapter = BeerInfoAdapter(infoes, requireContext());

        var listView = root.findViewById<ListView>(R.id.beerList);

        listView.adapter = adapter;

        adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            var item = adapter.infoes!![position];

            item.isExpanded = !item.isExpanded

            adapter.notifyDataSetChanged()

        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            removeBeerInfo(adapter.infoes!![position])

            infoes.removeAt(position)

            adapter.notifyDataSetChanged()

            true
        }

        return root
    }

    public fun loadBeerInfo(): ArrayList<BeerInfoAdapter.BeerInfo> {

        var result = ArrayList<BeerInfoAdapter.BeerInfo>()

        var database = DataBase(context)

        var cursor =
            database.readableDatabase.rawQuery("SELECT * FROM " + database.TABLE_BEER, null)

        if (cursor.moveToFirst()) {

            do {

                var name = cursor.getString(cursor.getColumnIndex("name"))

                var description = cursor.getString(cursor.getColumnIndex("description"))

                var photoBytes = cursor.getBlob(cursor.getColumnIndex("photo"))

                var photo = BitmapDrawable(
                    getResources(),
                    BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
                )

                result.add(BeerInfoAdapter.BeerInfo(name, photo, description))


            } while (cursor.moveToNext())
        }

        return result;

    }

    public fun removeBeerInfo(info: BeerInfoAdapter.BeerInfo) {

        var database = DataBase(context)

        var querryStr =
            "DELETE FROM " + database.TABLE_BEER + " WHERE name = ? AND description = ? "

        var querry = database.writableDatabase.compileStatement(querryStr);

        querry.bindString(1, info.title);
        querry.bindString(2, info.description);

        if (querry.executeInsert() < 0) {
            System.out.println("Failed to delete")

            Toast.makeText(context, "Failed to remove beer info", Toast.LENGTH_SHORT).show()
        } else {
            System.out.println("Deleted succesfully")

            Toast.makeText(context, "Removed beer info", Toast.LENGTH_SHORT).show()
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