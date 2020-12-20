package com.example.beerproject.activities.ui.beerCatalogOnline.beerInfoList

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.beerproject.R

class BeerInfoAdapter : BaseAdapter {

    class BeerInfo {
        public var title: String = "dummy title";
        public var photo: Drawable? = null;
        public var description: String = "dummy description";
        public var isExpanded = false;

        constructor(title: String, photo: Drawable?, description: String) {
            this.title = title
            this.photo = photo
            this.description = description
        }
    }

    public var infoes: ArrayList<BeerInfo>? = null;
    private var context: Context? = null;
    private var inflanter: LayoutInflater? = null;

    constructor(infoes: ArrayList<BeerInfo>?, context: Context) : super() {
        this.infoes = infoes
        this.context = context;
        this.inflanter =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var vi = convertView;

        if (vi == null) {
            vi = inflanter!!.inflate(R.layout.beer_info, null);
        }

        var title = vi!!.findViewById<TextView>(R.id.textTitle);
        var description = vi!!.findViewById<TextView>(R.id.textDescription);
        var beerPhoto = vi!!.findViewById<ImageView>(R.id.beerPhoto);

        var item = infoes!![position];

        title.text = item.title;
        description.text = item.description;
        beerPhoto.setImageDrawable(item.photo);

        if (!item.isExpanded) {
            vi!!.findViewById<LinearLayout>(R.id.AdditionalBeerInfo).visibility = View.GONE;
        } else {
            vi!!.findViewById<LinearLayout>(R.id.AdditionalBeerInfo).visibility = View.VISIBLE;
        }
        return vi;

    }

    override fun getItem(position: Int): Any {
        return infoes!![position];
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getCount(): Int {
        return infoes!!.size;
    }


}