package com.example.sharedcard.ui.bottom_navigation.check.array_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sharedcard.database.entity.category.CategoryEntity

class CategoryArrayAdapter(private val categories: List<CategoryEntity>) :
    BaseAdapter(), Filterable {

    override fun getCount() = categories.size

    override fun getItem(position: Int) = categories[position].name

    override fun getItemId(position: Int) = categories[position].id



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if(convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(
                android.R.layout.simple_dropdown_item_1line,
                parent,
                false
            )
        } else {
            view = convertView
        }
        val category = categories[position]
        val text = (view.findViewById(android.R.id.text1) as TextView)
        text.setText(category.name)
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