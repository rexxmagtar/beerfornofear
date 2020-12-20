package com.example.beerproject.activities.events

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beerproject.R
import java.util.*


class EventAdapter(var context: Context, p: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    var Events: ArrayList<Event> = p

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, viewGroup, false))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.name.text = Events[i].name
        myViewHolder.description.text = Events[i].description
        myViewHolder.date.text = Events[i].date
        myViewHolder.id.text = "ID: " + Events[i].id.toString()

        val getTitleEvent: String? = Events[i].name
        val getDescEvent: String? = Events[i].description
        val getDateEvent: String? = Events[i].date
        val getIdEvent: Number? = Events[i].id

        myViewHolder.itemView.setOnClickListener {
            val aa = Intent(context, EditEventActivity::class.java)

            aa.putExtra("name_event", getTitleEvent)
            aa.putExtra("description", getDescEvent)
            aa.putExtra("date", getDateEvent)
            aa.putExtra("id", getIdEvent)

            context.startActivity(aa)
        }
    }

    override fun getItemCount(): Int {
        return Events.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.name) as TextView  // name of event
        var description: TextView = itemView.findViewById<View>(R.id.description) as TextView  // description of event
        var date: TextView = itemView.findViewById<View>(R.id.date) as TextView // date of event
        var id: TextView = itemView.findViewById<View>(R.id.id_event) as TextView // id of event

    }

}