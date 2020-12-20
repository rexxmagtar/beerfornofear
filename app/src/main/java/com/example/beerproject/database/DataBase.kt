package com.example.beerproject.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase(context: Context) : SQLiteOpenHelper(context, "beer_catalog.db", null, 4) {

    private val TABLE_EVENT = "Event"
    private val TABLE_BEER = "Beer"

    val createDBRequest = "CREATE TABLE $TABLE_EVENT (" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "name_event varchar," +
            "description text," +
            "date datetime" +
            ");" +
            "" +
            "CREATE TABLE $TABLE_BEER (" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "name varchar PRIMARY KEY AUTOINCREMENT," +
            "photo blob PRIMARY KEY AUTOINCREMENT," +
            "description text PRIMARY KEY AUTOINCREMENT" +
            ");"

    fun insertIntoEventTable(name_event: String, description_event: String, date_event: String) {
        val values = ContentValues()

        values.put("name_event", name_event)
        values.put("description", description_event)
        values.put("date", date_event)

        writableDatabase.insert(TABLE_EVENT, null, values);
    }

    fun getAllFromEventTable(): Cursor {
        return readableDatabase.query(TABLE_EVENT,
                arrayOf("ID", "name_event", "description", "date"),
                null,
                null,
                null,
                null,
                null);
    }

    fun updateDataInEventTable(id_event: String, name_event: String,
                               description_event: String, date_event: String) {
        val values = ContentValues()

        values.put("name_event", name_event)
        values.put("description", description_event)
        values.put("date", date_event)

        writableDatabase.update(TABLE_EVENT, values, "ID = ?", arrayOf(id_event))
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createDBRequest)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}

