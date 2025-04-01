package com.example.sharedcard.ui.purchase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sharedcard.database.entity.shop.ShopEntity

class ShopArrayAdapter(private val shops: List<ShopEntity>) :
    BaseAdapter(), Filterable {

    override fun getCount() = shops.size

    override fun getItem(position: Int) = shops[position].name

    override fun getItemId(position: Int) = shops[position].id.toLong()


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
        val shop = shops[position]
        val text = (view.findViewById(android.R.id.text1) as TextView)
        text.text = shop.name
        return view
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = shops
            filterResults.count = shops.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}