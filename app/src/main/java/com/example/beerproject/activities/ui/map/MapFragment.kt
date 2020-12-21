package com.example.beerproject.activities.ui.map

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.example.beerproject.activities.ui.map.models.Place
import com.example.beerproject.activities.ui.map.models.UserMap
import java.io.*
import com.example.beerproject.R

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
private const val FILENAME = "UserMaps.data"
private const val REQUEST_CODE = 1234
private const val TAG = "MapFragment"

class MapFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var userMaps: MutableList<UserMap>
    private lateinit var mapAdapter: MapsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_map, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)


        userMaps = deserializeUserMaps(this).toMutableList()
        // Set layout manager on the recycler view
        rvMaps.layoutManager = LinearLayoutManager(this)
        // Set adapter on the recycler view
        mapAdapter = MapsAdapter(this, userMaps, object: MapsAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                // When user taps on view in RV, navigate to new activity
                val intent = Intent(this@MapFragment, DisplayMapActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, userMaps[position])
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            override fun onItemLongClick(position: Int) {
                Log.i(TAG, "onItemLongClick at position $position")
                val dialog =
                    AlertDialog.Builder(this@MapFragment)
                        .setTitle("Delete this map?")
                        .setMessage("Are you sure you want to delete this map?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", null)
                        .show()
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    userMaps.removeAt(position)
                    mapAdapter.notifyItemRemoved(position)
                    mapAdapter.notifyItemRangeChanged(position, mapAdapter.itemCount)
                    serializeUserMaps(this@MapFragment, userMaps)
                    dialog.dismiss()
                }
            }
        })
        rvMaps.adapter = mapAdapter

        fabCreateMap.setOnClickListener {
            Log.i(TAG, "Tap on FAB")
            showAlertDialog()
        }

        return root
    }

    private fun showAlertDialog() {
        val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map, null)
        val dialog =
            AlertDialog.Builder(this)
                .setTitle("Map title")
                .setView(mapFormView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = mapFormView.findViewById<EditText>(R.id.etTitle).text.toString()
            if (title.trim().isEmpty()) {
                Toast.makeText(this, "Map must have a non-empty title", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // Navigate to create map activity
            val intent = Intent(this@MapFragment, CreateMapActivity::class.java)
            intent.putExtra(EXTRA_MAP_TITLE, title)
            startActivityForResult(intent, REQUEST_CODE)
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get new map data from the data
            val userMap = data?.getSerializableExtra(EXTRA_USER_MAP) as UserMap
            Log.i(TAG, "onActivityResult with new map title ${userMap.title}")
            userMaps.add(userMap)
            mapAdapter.notifyItemInserted(userMaps.size - 1)
            serializeUserMaps(this, userMaps)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun serializeUserMaps(context: Context, userMaps: List<UserMap>) {
        Log.i(TAG, "serializeUserMaps")
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use { it.writeObject(userMaps) }
    }

    private fun deserializeUserMaps(context: Context) : List<UserMap> {
        Log.i(TAG, "deserializeUserMaps")
        val dataFile = getDataFile(context)
        if (!dataFile.exists()) {
            Log.i(TAG, "Data file does not exist yet")
            return emptyList()
        }
        ObjectInputStream(FileInputStream(dataFile)).use { return it.readObject() as List<UserMap> }
    }

    private fun getDataFile(context: Context) : File {
        Log.i(TAG, "Getting file from directory ${context.filesDir}")
        return File(context.filesDir, FILENAME)
    }

    private fun generateSampleData(): List<UserMap> {
        return listOf(
            UserMap(
                "test",
                listOf(
                    Place("Testing", "Best dorm at Stanford", 37.426, -122.163),
                )
            ),
        )
    }
}
