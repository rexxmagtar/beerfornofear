package com.example.beerproject.activities.ui.events

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beerproject.R
import com.example.beerproject.activities.events.Event
import com.example.beerproject.activities.events.EventAdapter
import com.example.beerproject.activities.events.NewEvent
import com.example.beerproject.database.DataBase

class EventsFragment : Fragment() {

    private lateinit var eventsViewModel: EventsViewModel
    var test: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Events"

        eventsViewModel =
                ViewModelProvider(this).get(EventsViewModel::class.java)
        val root = inflater.inflate(R.layout.activity_notification_main, container, false)
        test = root

        initComponents()

        btnAddNew!!.setOnClickListener {
            val a = Intent(context, NewEvent::class.java)
            startActivity(a)
        }

        // get data from DB.
        val cursor = dbHelper!!.getAllFromEventTable()

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("ID")
            val nameEventIndex = cursor.getColumnIndex("name_event")
            val descriptionIndex = cursor.getColumnIndex("description")
            val dateIndex = cursor.getColumnIndex("date")

            do {
                val event = Event(cursor.getInt(idIndex),
                    cursor.getString(nameEventIndex),
                    cursor.getString(descriptionIndex),
                    cursor.getString(dateIndex))

                list!!.add(event)
            } while (cursor.moveToNext())

            notificationAdapter = EventAdapter(requireContext(), list!!)
            ourdoes!!.adapter = notificationAdapter

            notificationAdapter!!.notifyDataSetChanged()
        }

        return root
    }

    var titlepage: TextView? = null
    var subtitlepage:TextView? = null
    var endpage:TextView? = null
    var btnAddNew: Button? = null

    var ourdoes: RecyclerView? = null
    var list: ArrayList<Event>? = null
    var notificationAdapter: EventAdapter? = null

    var dbHelper: DataBase? = null

    private fun initComponents() {
        titlepage = test?.findViewById(R.id.titlepage)
        subtitlepage = test?.findViewById(R.id.subtitlepage)
        btnAddNew = test?.findViewById(R.id.btnAddNew)

        ourdoes = test?.findViewById(R.id.ourdoes)
        ourdoes!!.layoutManager = LinearLayoutManager(context)
        list = ArrayList()

        // get data from db
        dbHelper = DataBase(context)
    }
}