package com.example.beerproject.activities.events

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.beerproject.R
import com.example.beerproject.database.DataBase

class EditEventActivity: AppCompatActivity() {

    var name_event: EditText? = null
    var description_event: EditText? = null
    var date_event: EditText? = null
    var id_event: EditText? = null

    var btnSaveUpdate: Button? = null
    var btnCancel: Button? = null

    var dbHelper: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_edit)

        name_event = findViewById(R.id.name_event)
        description_event = findViewById(R.id.desc_event)
        date_event = findViewById(R.id.date_event)
        id_event = findViewById(R.id.id_event)

        btnSaveUpdate = findViewById(R.id.btnUpdateEvent)
        btnCancel = findViewById(R.id.btnCancel)

        val id_text = intent.getStringExtra("id").toString()
        val name_event_text = intent.getStringExtra("name_event").toString()
        val description_event_text = intent.getStringExtra("description").toString()
        val date_event_text = intent.getStringExtra("date").toString()

        name_event!!.setText(name_event_text)
        description_event!!.setText(description_event_text)
        date_event!!.setText(date_event_text)

        dbHelper = DataBase(this)

        btnSaveUpdate!!.setOnClickListener {
            dbHelper!!.updateDataInEventTable(id_text,
                    name_event_text,
                    description_event_text,
                    date_event_text)

            val a = Intent(this@EditEventActivity, ListEventActivity::class.java)
            startActivity(a)
        }
    }
}

