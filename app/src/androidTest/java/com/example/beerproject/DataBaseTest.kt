package com.example.beerproject

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.beerproject.database.DataBase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DataBaseTest {

    @Test
    fun testDBUpdateRow() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // assertEquals("com.example.beerproject", appContext.packageName)
        val dbHelper = DataBase(appContext)

        val idVodka = dbHelper.insertIntoEventTable("drink_vodka",
            "Svayak is coming", "31.12.2020 23:55")

        assertEquals(1, dbHelper.updateDataInEventTable(idVodka.toString(), "Drink Every Day",
        "GO DRINK!", "25.12.2020 12:35"))

        dbHelper.deleteRowFromEventTable(idVodka.toString())
    }

    @Test
    fun testDBDeleteRow() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val dbHelper = DataBase(appContext)

        val idVodka = dbHelper.insertIntoEventTable("drink_vodka",
            "Svayak is coming", "31.12.2020 23:55")

        assertEquals(1, dbHelper.deleteRowFromEventTable(idVodka.toString()))
    }

    @Test
    fun testDBInsertRow() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val dbHelper = DataBase(appContext)

        val idVodka = dbHelper.insertIntoEventTable("drink_vodka",
            "Svayak is coming", "31.12.2020 23:55")

        assertNotEquals(-1, dbHelper.deleteRowFromEventTable(idVodka.toString()))

        dbHelper.deleteRowFromEventTable(idVodka.toString())
    }
}