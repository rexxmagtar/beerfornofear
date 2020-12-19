package com.example.beerproject.activities.ui.beerCatalogFavorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beerproject.R

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
        
        return root
    }
}