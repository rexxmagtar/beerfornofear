package com.example.beerproject.activities.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.beerproject.R
import com.example.beerproject.activities.BaseAcitivity
import com.example.beerproject.activities.EXTRA_NAV_FRAGMENT_ID_KEY
import com.example.beerproject.database.DataBase
import java.util.*


class NewEvent : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var name_event: EditText? = null
    private var description_event: EditText? = null

    var btnSaveTask: Button? = null
    var btnCancel: Button? = null
    var btnSetDate: Button? = null

    var dbHelper: DataBase? = null

    var date_event: String? = "21.12.2020 4:34"

    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val month_ed = month + 1

        date_event = "$dayOfMonth.$month_ed.$year "

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this@NewEvent, this@NewEvent, hour, minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        date_event += if (hourOfDay < 10) {
            "0$hourOfDay:"
        } else {
            "$hourOfDay:"
        }

        date_event += if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        }
    }

    private fun initComponents() {
        name_event = findViewById(R.id.name_event)
        description_event = findViewById(R.id.desc_event)

        btnSaveTask = findViewById(R.id.btnUpdateEvent)
        btnCancel = findViewById(R.id.btnCancel)
        btnSetDate = findViewById(R.id.btnDateEvent)

        dbHelper = DataBase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notification)
        initComponents()
        title = "Create New Event"

        btnSetDate!!.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH) + 1
            year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this@NewEvent, this@NewEvent, year, month, day)

            dpd.show()
        }

        btnSaveTask!!.setOnClickListener {
            dbHelper!!.insertIntoEventTable(name_event!!.text.toString(),
                    description_event!!.text.toString(),
                    date_event!!.toString()
            )

            val a = Intent(this@NewEvent, BaseAcitivity::class.java)

            a.putExtra(EXTRA_NAV_FRAGMENT_ID_KEY,R.id.nav_events)

            startActivity(a)
        }

        btnCancel!!.setOnClickListener {
            val a = Intent(this@NewEvent, BaseAcitivity::class.java)

            a.putExtra(EXTRA_NAV_FRAGMENT_ID_KEY,R.id.nav_events)

            startActivity(a)
        }

    }
}
