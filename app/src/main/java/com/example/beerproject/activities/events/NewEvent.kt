package com.example.beerproject.activities.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.beerproject.R
import com.example.beerproject.database.DataBase
import java.util.*


class NewEvent : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    var titlepage: TextView? = null
    var addtitle: TextView? = null
    var adddesc: TextView? = null
    var adddate: TextView? = null

    private var name_event: EditText? = null
    private var description_event: EditText? = null

    var btnSaveTask: Button? = null
    var btnCancel: Button? = null
    var btnSetDate: Button? = null

    var dbHelper: DataBase? = null

    var doesNum = Random().nextInt()
    var keydoes = doesNum.toString()

    var date_event: String? = "01 01, 2012"

    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date_event = "$dayOfMonth.$month.$year "

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@NewEvent, this@NewEvent, hour, minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        date_event += "$hourOfDay:$minute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notification)

        btnSetDate = findViewById(R.id.btnDateEvent)

        btnSetDate!!.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this@NewEvent, this@NewEvent, year, month, day)

            dpd.show()
        }

        titlepage = findViewById(R.id.titlepage)
        addtitle = findViewById(R.id.addtitle)
        adddesc = findViewById(R.id.adddesc)
        adddate = findViewById(R.id.adddate)

        name_event = findViewById(R.id.name_event)
        description_event = findViewById(R.id.desc_event)

        btnSaveTask = findViewById(R.id.btnUpdateEvent)
        btnCancel = findViewById(R.id.btnCancel)

        dbHelper = DataBase(this)

        btnSaveTask!!.setOnClickListener {
            dbHelper!!.insertIntoEventTable(name_event!!.text.toString(),
                    description_event!!.text.toString(),
                    date_event!!.toString()
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
