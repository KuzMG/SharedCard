package com.example.sharedcard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sharedcard.database.entity.shop.ShopEntity

class MetricArrayAdapter(private val metrics: List<String>) :
    BaseAdapter(), Filterable {

    override fun getCount() = metrics.size

    override fun getItem(position: Int) = metrics[position]
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            inflater.inflate(
                android.R.layout.simple_dropdown_item_1line,
                parent,
                false
            )
        } else {
            convertView
        }
        val text = (view.findViewById(android.R.id.text1) as TextView)
        text.text = metrics[position]
        return view
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = metrics
            filterResults.count = metrics.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}