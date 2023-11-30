package com.example.sharedcard.ui.check.check_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R

open class ViewHolderGeneral<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val checkBox = itemView.findViewById<CheckBox>(R.id.checkbox)
    protected val nameTextView = itemView.findViewById<TextView>(R.id.name_item)
    protected val categoryTextView = itemView.findViewById<TextView>(R.id.category_item)
    protected val userTextView = itemView.findViewById<TextView>(R.id.user_item)
    protected val quantityPriceTextView = itemView.findViewById<TextView>(R.id.item_quantity_or_price)
    protected val dateTextView = itemView.findViewById<TextView>(R.id.item_date)
    open fun onBind(item: T) {}
}

abstract class ListAdapterGeneral<T, U : ViewHolderGeneral<T>>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, U>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): U {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.list_item_check,
                parent,
                false
            )

        return ViewHolderGeneral<T>(view) as U
    }

    override fun onBindViewHolder(holder: U, position: Int) {
        holder.onBind(getItem(position))
    }
}