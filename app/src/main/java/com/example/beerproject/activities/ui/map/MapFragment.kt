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
import com.example.beerproject.activities.ui.map.models.UserMap
import com.example.beerproject.activities.ui.map.models.Place


const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
private const val FILENAME = "UserMaps.data"
private const val REQUEST_CODE = 1234
private const val TAG = "MainActivity"

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var userMap: List<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_map, container, false)


        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this);


        return root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        userMap = generateSampleData()
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

    private fun generateSampleData(): List<Place> {
        return listOf(
            Place(
                "Хоспер-ХОПС-ПАБ",
                "улица Колесникова 38, Минск. Открыто 11:00-00:00",
                53.92164000035742,
                27.465255547841455
            ),
            Place(
                "Пивная № 1",
                "ул. Кальварийская 44, 220079, Минск. Открыто 11:00-00:00",
                53.915169747790905,
                27.514007375378394
            ),
            Place(
                "CUBA BAR",
                "ул. Мележа 5/1, Минск. Открыто 11:00-00:00",
                53.946702759508966,
                27.594344893995608
            ),
            Place(
                "План Б",
                "Дом прессы, вуліца Багдана Хмяльніцкага 10А, Мінск. Открыто 11:00-00:00",
                53.93336477763946,
                27.595031539453878
            ),
            Place(
                "ID bar",
                "ул. Захарова 19, Минск 220034. Открыто 11:00-00:00",
                53.91719180942732,
                27.578552048455474
            ),
        )
    }
}