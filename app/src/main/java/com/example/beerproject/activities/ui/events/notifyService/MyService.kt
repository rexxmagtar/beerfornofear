package com.example.beerproject.activities.ui.events.notifyService

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.beerproject.activities.BaseAcitivity
import com.example.beerproject.activities.ui.events.Event
import com.example.beerproject.database.DataBase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MyService : Service() {
    var nm: NotificationManager? = null

    private var notificationPeriodSeconds = 60*60*1;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        startMyOwnForeground()

        Log.println(Log.INFO, "SERVICE", "CREATED")
        nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        loadHolidayInfo { }

        GlobalScope.launch {
            updateThread()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            Log.println(Log.INFO, "SERVICE", "STARTED TO WORK")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotif(title: String, description: String) {

        // 3-я часть
        val intent = Intent(this, BaseAcitivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notif = Notification.Builder(this, "all_notifications")
            .setSmallIcon(R.drawable.alert_dark_frame)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pIntent).build()

        Log.println(Log.INFO, "SERVICE", "SENDING")
        // отправляем
        nm!!.notify(322, notif)
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHolidayInfo(listener: (ArrayList<Event>) -> Unit) {


        DataBase(this).insertIntoEventTable(
            "Ivan piva",
            "Ivan is waiting for you",
            "21.12.2020 21:00"
        )

        var result = ArrayList<Event>();

        var finished = false;


        CoroutineScope(Dispatchers.Default).launch {
            Log.println(Log.ERROR, "SOME TAG", "event loading!!!  ");
            var url =
                "https://calendarific.com/api/v2/holidays?&api_key=b0eb134b88a9bba39cc3e06a850d333cab6f0493&country=BY&year=2020";

//            val params = JSONObject()
//            params.put("x-rapidapi-key", "03f3fe5662msh1e87153aa5db028p1a84d8jsn7d5919c12887")
//            params.put("x-rapidapi-host", "brianiswu-open-brewery-db-v1.p.rapidapi.com")

            var request = JsonObjectRequest(url, null, {


                CoroutineScope(Dispatchers.IO).launch {
                    System.out.println("Loaded holiday.")

                    var holidayList = it.getJSONObject("response").getJSONArray("holidays");

                    System.out.println("Finished to load. Loaded " + holidayList.length() + " holidays")

                    for (i in 0 until (holidayList.length())) {

                        Log.println(Log.INFO, "SERVICE", "parsing $i")

                        var name = holidayList.getJSONObject(i).getString("name")
                        var description = holidayList.getJSONObject(i).getString("description")
                        var year =
                            holidayList.getJSONObject(i).getJSONObject("date")
                                .getJSONObject("datetime").getString("year")
                        var month = holidayList.getJSONObject(i).getJSONObject("date")
                            .getJSONObject("datetime")
                            .getString("month")
                        var day =
                            holidayList.getJSONObject(i).getJSONObject("date")
                                .getJSONObject("datetime").getString("day")

                        var dateIso =
                            holidayList.getJSONObject(i).getJSONObject("date").getString("iso")

                        Log.println(Log.INFO, "SERVICE", "Found holiday $dateIso")


                        if (!Regex("^\\s*((?:19|20)\\d{2})-(1[012]|0?[1-9])-(3[01]|[12][0-9]|0?[1-9])\\s*").matches(
                                dateIso
                            )
                        ) {
                            Log.println(Log.INFO, "SERVICE", "Skipped date $dateIso")
                            continue
                        }

                        val l = LocalDate.parse(dateIso, DateTimeFormatter.ofPattern("yyyy-MM-dd"))


                        var date = "$day.$month.$year 12:00";

                        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")

                        var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

                        date = l.format(formatter) + " 12:00"

                        var event =
                            Event(
                                null,
                                name,
                                description,
                                date
                            )

                        DataBase(this@MyService).insertIntoEventTable(name, description, date)

                        result!!.add(event)

                    }

                    finished = true;
                }

            }, {

                Log.println(Log.ERROR, "SOME TAG", "event load error!!!  " + it.message);

                finished = true;

            })

            val queue = Volley.newRequestQueue(this@MyService)

            queue.add(request)

            while (!finished) {
//Waiting
                delay(1)
                /* println("Waiting")*/
            }

            listener.invoke(result);

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateThread() {
        Log.println(Log.INFO, "SERVICE", "CHECKING HOLIDAYS START")
        while (true) {

            Log.println(Log.INFO, "SERVICE", "CHECKING HOLIDAYS SLEEPEING")
            delay((notificationPeriodSeconds * 1000).toLong())

            Log.println(Log.INFO, "SERVICE", "CHECKING HOLIDAYS UNSLEEPED")

            Log.println(Log.INFO, "SERVICE", "CHECKING HOLIDAYS")

            var eventsCursor = DataBase(this).getAllFromEventTable()

            if (eventsCursor.moveToFirst()) {

                do {

                    var name = eventsCursor.getString(eventsCursor.getColumnIndex("name_event"))

                    var description =
                        eventsCursor.getString(eventsCursor.getColumnIndex("description"))

                    var date = eventsCursor.getString(eventsCursor.getColumnIndex("date"))

//                    Log.println(Log.INFO, "SERVICE", "date: $date")

                    val l =
                        LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

                    var currentDate = LocalDate.now()

                    if (currentDate.dayOfYear == l.dayOfYear && currentDate.year == l.year) {

                        Log.println(Log.INFO, "SERVICE", "Found holiday to send $name")

                        sendNotif("IT'S TIME TO DRINK: " + name, description)
                    }

                } while (eventsCursor.moveToNext())

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.println(Log.INFO, "SERVICESTOP", "DESTROYED")
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        var NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        var channelName = "My Background Service";
        var chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        );
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        manager.createNotificationChannel(chan);

        var notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        var notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.arrow_down_float)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build();

        startForeground(2, notification);
    }
}