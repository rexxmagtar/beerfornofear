package com.example.beerproject.activities.ui.beerCatalogFavorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beerproject.R
import com.example.beerproject.activities.ui.beerCatalogOnline.beerInfoList.BeerInfoAdapter

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

            true
        }

        return root
    }

    public fun loadBeerInfo(): Array<BeerInfoAdapter.BeerInfo> {

        var result = arrayOf<BeerInfoAdapter.BeerInfo>(BeerInfoAdapter.BeerInfo("pivo1",resources.getDrawable(R.drawable.vodka),"pivo1 description"));

        //TODO: code to load here

        return result;

    }

    public fun removeBeerInfo(info:BeerInfoAdapter.BeerInfo){

    }
}