package com.kote.martin.wats.adapter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ArrayAdapter
import com.kote.martin.wats.R
import com.kote.martin.wats.model.Place


class PlaceListViewAdapter(context: Context) : ArrayAdapter<Place>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val location = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_location, parent, false)
        }
        // Lookup view for data population
        val tvName = convertView!!.findViewById(R.id.location_name) as TextView
        val tvId = convertView.findViewById(R.id.place_id) as TextView
        val tvgmapsId = convertView.findViewById(R.id.gmapsId) as TextView
        // Populate the data into the template view using the data object
        tvName.text = location!!.name
        tvId.text = location!!.id.toString()
        tvgmapsId.text = location!!.gmapsId
        // Return the completed view to render on screen
        return convertView
    }


}
