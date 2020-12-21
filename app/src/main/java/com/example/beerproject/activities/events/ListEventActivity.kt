package com.example.beerproject.activities.events

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beerproject.R
import com.example.beerproject.database.DataBase


class ListEventActivity: AppCompatActivity() {
    var titlepage: TextView? = null
    var subtitlepage:TextView? = null
    var endpage:TextView? = null
    var btnAddNew: Button? = null

    var ourdoes: RecyclerView? = null
    var list: ArrayList<Event>? = null
    var notificationAdapter: EventAdapter? = null

    var dbHelper: DataBase? = null

    fun initComponents() {
        titlepage = findViewById(R.id.titlepage)
        subtitlepage = findViewById(R.id.subtitlepage)
        btnAddNew = findViewById(R.id.btnAddNew)

        ourdoes = findViewById(R.id.ourdoes)
        ourdoes!!.setLayoutManager(LinearLayoutManager(this))
        list = ArrayList()

        // get data from db
        dbHelper = DataBase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_main)

        initComponents()

        btnAddNew!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val a = Intent(this@ListEventActivity, NewEvent::class.java)
                startActivity(a)
            }
        })

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

            notificationAdapter = EventAdapter(this@ListEventActivity, list!!)
            ourdoes!!.adapter = notificationAdapter


            notificationAdapter!!.notifyDataSetChanged()
        }
    }
}


