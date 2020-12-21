package com.example.beerproject.activities.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.transition.FragmentTransitionSupport
import com.example.beerproject.R
import com.example.beerproject.activities.BaseAcitivity
import com.example.beerproject.activities.ui.events.EventsFragment
import com.example.beerproject.database.DataBase
import java.util.*


class EditEventActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    var name_event: EditText? = null
    var description_event: EditText? = null
    var id_event: EditText? = null

    var date_event: String? = "21.12.2020 4:34"

    var btnSaveUpdate: Button? = null
    var btnCancel: Button? = null
    var btnSetDate: Button? = null
    var btnDeleteEvent: Button? = null

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
        val timePickerDialog = TimePickerDialog(
            this@EditEventActivity, this@EditEventActivity,
            hour, minute, DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        date_event += "$hourOfDay:$minute"
    }

    private fun initComponents() {
        name_event = findViewById(R.id.name_event_edit)
        description_event = findViewById(R.id.desc_event_edit)

        id_event = findViewById(R.id.id_event)

        btnSaveUpdate = findViewById(R.id.btnUpdateEvent)
        btnCancel = findViewById(R.id.btnCancel)
        btnSetDate = findViewById(R.id.btnDateEvent)
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_edit)
        initComponents()
        title = "Edit Event"

        btnSetDate!!.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(
                this@EditEventActivity,
                this@EditEventActivity,
                year,
                month,
                day
            )

            dpd.show()
        }

        val id_text = intent.getStringExtra("ID").toString()
        val name_event_text = intent.getStringExtra("name_event").toString()
        val description_event_text = intent.getStringExtra("description").toString()

        name_event!!.setText(name_event_text)
        description_event!!.setText(description_event_text)

        dbHelper = DataBase(this)

        btnSaveUpdate!!.setOnClickListener {
            val name_event_text_1 = name_event!!.text.toString()
            val description_event_text_1 = description_event!!.text.toString()

            dbHelper!!.updateDataInEventTable(
                id_text,
                name_event_text_1,
                description_event_text_1,
                date_event!!.toString()
            )

            val a = Intent(this@EditEventActivity, BaseAcitivity::class.java)
            startActivity(a)
        }

        btnCancel!!.setOnClickListener {
            val a = Intent(this@EditEventActivity, BaseAcitivity::class.java)
            startActivity(a)
        }

        btnDeleteEvent!!.setOnClickListener {
            dbHelper!!.deleteRowFromEventTable(id_text)

            val a = Intent(this@EditEventActivity, BaseAcitivity::class.java)
            startActivity(a)
        }
    }
}

