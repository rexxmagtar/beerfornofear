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
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beerproject.activities.ui.map.models.Place
import com.example.beerproject.activities.ui.map.models.UserMap
import java.io.*
import com.example.beerproject.R
import com.example.beerproject.activities.ui.events.EventsViewModel
import com.example.beerproject.database.DataBase
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
private const val FILENAME = "UserMaps.data"
private const val REQUEST_CODE = 1234
private const val TAG = "MapFragment"
class MapFragment : Fragment() {

    private lateinit var userMaps: MutableList<UserMap>
    private lateinit var mapAdapter: MapsAdapter

    var rvMaps: RecyclerView? = null
    var fabCreateMap: FloatingActionButton? = null

    var test: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        test = root

        initComponents()


        userMaps = context?.let { deserializeUserMaps(it).toMutableList() }!!
        // Set layout manager on the recycler view
        rvMaps?.layoutManager = LinearLayoutManager(context)
        // Set adapter on the recycler view
        mapAdapter = MapsAdapter(requireContext(), userMaps, object: MapsAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                // When user taps on view in RV, navigate to new activity
                val intent = Intent(context, DisplayMapActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, userMaps[position])
                startActivity(intent)
            }

            override fun onItemLongClick(position: Int) {
                Log.i(TAG, "onItemLongClick at position $position")
                val dialog =
                    AlertDialog.Builder(context!!)
                        .setTitle("Delete this map?")
                        .setMessage("Are you sure you want to delete this map?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", null)
                        .show()
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    userMaps.removeAt(position)
                    mapAdapter.notifyItemRemoved(position)
                    mapAdapter.notifyItemRangeChanged(position, mapAdapter.itemCount)
                    serializeUserMaps(context!!, userMaps)
                    dialog.dismiss()
                }
            }
        })
        rvMaps?.adapter = mapAdapter

        fabCreateMap?.setOnClickListener {
            Log.i(TAG, "Tap on FAB")
            showAlertDialog()
        }
        return fabCreateMap
    }

    fun initComponents() {
        rvMaps = test?.findViewById(R.id.rvMaps)
        fabCreateMap = test?.findViewById(R.id.fabCreateMap)
    }

    private fun showAlertDialog() {
        val mapFormView = LayoutInflater.from(context).inflate(R.layout.dialog_create_map, null)
        val dialog =
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Map title")
                    .setView(mapFormView)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", null)
                    .show()
            }

        if (dialog != null) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val title = mapFormView.findViewById<EditText>(R.id.etTitle).text.toString()
                if (title.trim().isEmpty()) {
                    Toast.makeText(context, "Map must have a non-empty title", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                // Navigate to create map activity
                val intent = Intent(context, CreateMapActivity::class.java)
                intent.putExtra(EXTRA_MAP_TITLE, title)
                startActivityForResult(intent, REQUEST_CODE)
                dialog.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get new map data from the data
            val userMap = data?.getSerializableExtra(EXTRA_USER_MAP) as UserMap
            Log.i(TAG, "onActivityResult with new map title ${userMap.title}")
            userMaps.add(userMap)
            mapAdapter.notifyItemInserted(userMaps.size - 1)
            context?.let { serializeUserMaps(it, userMaps) }
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
                    Place("Branner Hall", "Best dorm at Stanford", 37.426, -122.163)
                )
            )
        )
    }
}
