package com.example.sharedcard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity

class CategoryArrayAdapter(private val categories: List<CategoryProductEntity>) :
    BaseAdapter(), Filterable {

    override fun getCount() = categories.size

    override fun getItem(position: Int) = categories[position].name

    override fun getItemId(position: Int) = categories[position].id.toLong()



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
        val category = categories[position]
        val text = (view.findViewById(android.R.id.text1) as TextView)
        text.text = category.name
        return view
    }

    override fun getFilter() = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = categories
            filterResults.count =categories.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}