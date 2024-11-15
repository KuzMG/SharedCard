package com.example.sharedcard.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.databinding.ListItemCategoryPopularBinding
import com.example.sharedcard.databinding.ListItemCategoryUnpopularBinding
import com.squareup.picasso.Picasso

abstract class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(item: List<CategoryEntity>)
}

class CategoryPopularViewHolder(
    private val binding: ListItemCategoryPopularBinding,
    private val nextFragment: (id: Int) -> Unit
) : CategoryViewHolder(binding.root) {

    override fun onBind(item: List<CategoryEntity>) {
        binding.one.run {
            try{
                val first = item[0]
                nameTextView.text = first.name
                Picasso.get().load(first.url).into(imageTextView)
                //cardView.setCardBackgroundColor(Color.parseColor(first.color))
                root.setOnClickListener {
                    nextFragment(first.id)
                }
            } catch (e: IndexOutOfBoundsException){
                binding.one.root.visibility = View.INVISIBLE
            }
        }
        binding.two.run {
            try {
                val second = item[1]
                nameTextView.text = second.name
                Picasso.get().load(second.url).into(imageTextView)
                //  cardView.setCardBackgroundColor(Color.parseColor(second.color))
                root.setOnClickListener {
                    nextFragment(second.id)
                }
            }  catch (e: IndexOutOfBoundsException){
                binding.two.root.visibility = View.INVISIBLE
            }

        }
    }
}

class CategoryUnpopularViewHolder(
    private val binding: ListItemCategoryUnpopularBinding,
    private val nextFragment: (id: Int) -> Unit
) :
    CategoryViewHolder(binding.root) {

    override fun onBind(item: List<CategoryEntity>) {
        binding.one.run {
            try {
                val first = item[0]
                nameTextView.text = first.name
                Picasso.get().load(first.url).into(imageTextView)
                // cardView.setCardBackgroundColor(Color.parseColor(first.color))
                root.setOnClickListener {
                    nextFragment(first.id)
                }
            }  catch (e: IndexOutOfBoundsException){
                binding.one.root.visibility = View.INVISIBLE
            }
        }
        binding.two.run {
            try {
                val second = item[1]
                nameTextView.text = second.name
                Picasso.get().load(second.url).into(imageTextView)
                // cardView.setCardBackgroundColor(Color.parseColor(second.color))
                root.setOnClickListener {
                    nextFragment(second.id)
                }
            }  catch (e: IndexOutOfBoundsException){
                binding.two.root.visibility = View.INVISIBLE
            }
        }
        binding.three.run {
            try {
                val three = item[2]
                nameTextView.text = three.name
                Picasso.get().load(three.url).into(imageTextView)
//            cardView.setCardBackgroundColor(Color.parseColor(three.color))
                root.setOnClickListener {
                    nextFragment(three.id)
                }
            }  catch (e: IndexOutOfBoundsException){
                binding.three.root.visibility = View.INVISIBLE
            }
        }
    }
}

class CategoryAdapter(
    private val list: List<List<CategoryEntity>>,
    private val click: (id: Int) -> Unit
) :
    RecyclerView.Adapter<CategoryViewHolder>() {
    override fun getItemViewType(position: Int) =
        when (position) {
            0, 1 -> 0
            else -> 1
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        when (viewType) {
            0 -> {
                val inflater =
                    LayoutInflater.from(parent.context)
                CategoryPopularViewHolder(
                    DataBindingUtil.inflate(
                        inflater,
                        R.layout.list_item_category_popular, parent, false
                    ), click
                )
            }

            1 -> {
                val inflater =
                    LayoutInflater.from(parent.context)
                CategoryUnpopularViewHolder(
                    DataBindingUtil.inflate(
                        inflater,
                        R.layout.list_item_category_unpopular, parent, false
                    ), click
                )
            }

            else -> throw IndexOutOfBoundsException()
        }


    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(list[position])
    }

}