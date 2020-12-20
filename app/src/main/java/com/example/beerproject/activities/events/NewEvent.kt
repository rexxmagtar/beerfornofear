package com.example.beerproject.activities.events

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.beerproject.R
import com.example.beerproject.database.DataBase
import java.util.*


class NewEvent : AppCompatActivity() {
    var titlepage: TextView? = null
    var addtitle: TextView? = null
    var adddesc: TextView? = null
    var adddate: TextView? = null

    private var name_event: EditText? = null
    private var description_event: EditText? = null
    private var date_event: EditText? = null

    var btnSaveTask: Button? = null
    var btnCancel: Button? = null

    var dbHelper: DataBase? = null

    var doesNum = Random().nextInt()
    var keydoes = doesNum.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notification)

        titlepage = findViewById(R.id.titlepage)
        addtitle = findViewById(R.id.addtitle)
        adddesc = findViewById(R.id.adddesc)
        adddate = findViewById(R.id.adddate)

        name_event = findViewById(R.id.name_event)
        description_event = findViewById(R.id.desc_event)
        date_event = findViewById(R.id.date_event)

        btnSaveTask = findViewById(R.id.btnUpdateEvent)
        btnCancel = findViewById(R.id.btnCancel)

        dbHelper = DataBase(this)

        btnSaveTask!!.setOnClickListener {
            dbHelper!!.insertIntoEventTable(name_event!!.text.toString(),
                    description_event!!.text.toString(),
                    date_event!!.text.toString()
            )

            val a = Intent(this@NewEvent, ListEventActivity::class.java)
            startActivity(a)
        }

        btnCancel!!.setOnClickListener {
            val a = Intent(this@NewEvent, ListEventActivity::class.java)
            startActivity(a)
        }

    }
}
