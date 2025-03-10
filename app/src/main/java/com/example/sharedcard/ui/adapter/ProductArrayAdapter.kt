package com.example.sharedcard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sharedcard.database.entity.product.ProductEntity

class ProductArrayAdapter(private val products: List<ProductEntity>) :
    BaseAdapter(), Filterable {

    override fun getCount() = products.size

    override fun getItem(position: Int) = products[position].name

    override fun getItemId(position: Int) = products[position].id.toLong()



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = if(convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            inflater.inflate(
                android.R.layout.simple_dropdown_item_1line,
                parent,
                false
            )
        } else {
            convertView
        }
        val product = products[position]
        val text = (view.findViewById(android.R.id.text1) as TextView)
        text.text = product.name
        return view
    }

    override fun getFilter() = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = products
            filterResults.count =products.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}