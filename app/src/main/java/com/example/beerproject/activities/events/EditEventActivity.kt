package com.example.beerproject.activities.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.beerproject.R
import com.example.beerproject.database.DataBase
import java.util.*

class EditEventActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    var name_event: EditText? = null
    var description_event: EditText? = null
    var id_event: EditText? = null

    var date_event: String? = "01 01, 2012"

    var btnSaveUpdate: Button? = null
    var btnCancel: Button? = null
    var btnSetDate: Button? = null

    var dbHelper: DataBase? = null

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date_event = "$dayOfMonth.$month.$year "

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@EditEventActivity, this@EditEventActivity,
            hour, minute, DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        date_event += "$hourOfDay:$minute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_edit)

        name_event = findViewById(R.id.name_event)
        description_event = findViewById(R.id.desc_event)

        id_event = findViewById(R.id.id_event)

        btnSaveUpdate = findViewById(R.id.btnUpdateEvent)
        btnCancel = findViewById(R.id.btnCancel)
        btnSetDate = findViewById(R.id.btnDateEvent)

        btnSetDate!!.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this@EditEventActivity, this@EditEventActivity, year, month, day)

            dpd.show()
        }

        val id_text = intent.getStringExtra("ID").toString()
        var name_event_text = intent.getStringExtra("name_event").toString()
        var description_event_text = intent.getStringExtra("description").toString()

        name_event!!.setText(name_event_text)
        description_event!!.setText(description_event_text)

        dbHelper = DataBase(this)

        btnSaveUpdate!!.setOnClickListener {
            name_event_text = name_event!!.text.toString()
            description_event_text = description_event!!.text.toString()

            dbHelper!!.updateDataInEventTable(id_text,
                    name_event_text,
                    description_event_text,
                    date_event.toString())

            val a = Intent(this@EditEventActivity, ListEventActivity::class.java)
            startActivity(a)
        }

        btnCancel!!.setOnClickListener {
            val a = Intent(this@EditEventActivity, ListEventActivity::class.java)
            startActivity(a)
        }
    }
}

