package com.example.beerproject.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase(context: Context?) : SQLiteOpenHelper(context, "beer_catalog.db", null, 4) {
    val TABLE_EVENT = "Event"
    val TABLE_BEER = "Beer"

    val createDBRequest = "CREATE TABLE $TABLE_EVENT (" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "name_event varchar," +
            "description text," +
            "date datetime" +
            ");"

    fun insertIntoEventTable(name_event: String, description_event: String, date_event: String): Long {
        val values = ContentValues()

        values.put("name_event", name_event)
        values.put("description", description_event)
        values.put("date", date_event)

//        if (readableDatabase.rawQuery("SELECT * FROM EVENT WHERE name_event = '$name_event'", null)
//                .moveToFirst()
//        ) {
//            return
//        }

       if( readableDatabase.query(
            "EVENT",
            arrayOf("name_event"),
            "name_event = ?",
            arrayOf(name_event),
            null,
            null,
            null
        ).moveToFirst()){
           return -1
       }

        return writableDatabase.insert(TABLE_EVENT, null, values);
    }

    fun getAllFromEventTable(): Cursor {
        return readableDatabase.query(
            TABLE_EVENT,
            arrayOf("ID", "name_event", "description", "date"),
            null,
            null,
            null,
            null,
            null
        );
    }

    fun updateDataInEventTable(
        id_event: String, name_event: String,
        description_event: String, date_event: String
    ): Int {
        val values = ContentValues()

        values.put("name_event", name_event)
        values.put("description", description_event)
        values.put("date", date_event)

        return writableDatabase.update(TABLE_EVENT, values, "ID=$id_event", null)
    }

    fun deleteRowFromEventTable(id_event: String): Int {
        return writableDatabase.delete(TABLE_EVENT, "ID=$id_event", null)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createDBRequest)
        db?.execSQL(
            "CREATE TABLE $TABLE_BEER (" +
                    "ID integer PRIMARY KEY AUTOINCREMENT," +
                    "name varchar," +
                    "photo blob ," +
                    "description text )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
