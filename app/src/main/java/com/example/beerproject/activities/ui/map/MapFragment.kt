package com.example.beerproject.activities.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beerproject.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.example.beerproject.activities.ui.map.models.Place

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var userMap: List<Place>
    private lateinit var mapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this);
        return root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        userMap = mapViewModel._place.value!!
        val boundsBuilder = LatLngBounds.Builder()
        for (place in userMap) {
            val latLng = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(latLng)
            mMap.addMarker(
                MarkerOptions().position(latLng).title(place.title).snippet(place.description)
            )
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
    }
}