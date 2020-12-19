package com.example.beerproject.activities.ui.beerCatalogOnline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beerproject.R

class BeerCatalogOnlineFragment : Fragment() {

    private lateinit var beerCatalogOnlineViewModel: BeerCatalogOnlineViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        beerCatalogOnlineViewModel =
                ViewModelProvider(this).get(BeerCatalogOnlineViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_beer_catalog_online, container, false)
        
        return root
    }
}