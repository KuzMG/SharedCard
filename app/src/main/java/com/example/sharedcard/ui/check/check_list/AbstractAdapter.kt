package com.example.sharedcard.ui.check.check_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R

open class ViewHolderGeneral(itemView: View) : ViewHolder(itemView) {
    protected val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    protected val nameTextView: TextView = itemView.findViewById(R.id.name_item)
    protected val categoryTextView: TextView = itemView.findViewById(R.id.category_item)
    protected val userTextView: TextView = itemView.findViewById(R.id.user_item)
    protected val quantityPriceTextView: TextView = itemView.findViewById(R.id.item_quantity_or_price)
    protected val dateTextView: TextView = itemView.findViewById(R.id.item_date)
}

abstract class ListAdapterGeneral<T>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T,ViewHolderGeneral>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.list_item_check,
                parent,
                false
            )
        return ViewHolderGeneral(view)
    }
}