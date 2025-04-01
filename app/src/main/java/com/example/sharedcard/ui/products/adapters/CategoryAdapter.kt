package com.example.sharedcard.ui.products.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.databinding.ListItemCategoryPopularBinding
import com.example.sharedcard.databinding.ListItemCategoryUnpopularBinding
import com.squareup.picasso.Picasso


class CategoryAdapter(
    private val list: List<List<CategoryProductEntity>>,
    private val click: (id: Int) -> Unit
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
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
                    ListItemCategoryPopularBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), click
                )
            }

            1 -> {
                val inflater =
                    LayoutInflater.from(parent.context)
                CategoryUnpopularViewHolder(
                    ListItemCategoryUnpopularBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), click
                )
            }

            else -> throw IndexOutOfBoundsException()
        }


    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(list[position])
    }


    class CategoryPopularViewHolder(
        private val binding: ListItemCategoryPopularBinding,
        private val nextFragment: (id: Int) -> Unit
    ) : CategoryViewHolder(binding.root) {

        override fun onBind(item: List<CategoryProductEntity>) {
            binding.one.run {
                try {
                    val first = item[0]
                    nameTextView.text = first.name
                    Picasso.get().load(first.url).into(imageTextView)
                    root.setOnClickListener {
                        nextFragment(first.id)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    binding.one.root.visibility = View.INVISIBLE
                }
            }
            binding.two.run {
                try {
                    val second = item[1]
                    nameTextView.text = second.name
                    Picasso.get().load(second.url).into(imageTextView)
                    root.setOnClickListener {
                        nextFragment(second.id)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    binding.two.root.visibility = View.INVISIBLE
                }

            }
        }
    }

    abstract class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBind(item: List<CategoryProductEntity>)
    }

    class CategoryUnpopularViewHolder(
        private val binding: ListItemCategoryUnpopularBinding,
        private val nextFragment: (id: Int) -> Unit
    ) :
        CategoryViewHolder(binding.root) {

        override fun onBind(item: List<CategoryProductEntity>) {
            binding.one.run {
                root.visibility = try {
                    val first = item[0]
                    nameTextView.text = first.name
                    Picasso.get().load(first.url).into(imageTextView)
                    root.setOnClickListener {
                        nextFragment(first.id)
                    }
                    View.VISIBLE
                } catch (e: IndexOutOfBoundsException) {
                    View.INVISIBLE
                }
            }
            binding.two.run {
                root.visibility = try {
                    val second = item[1]
                    nameTextView.text = second.name
                    Picasso.get().load(second.url).into(imageTextView)
                    root.setOnClickListener {
                        nextFragment(second.id)
                    }
                    View.VISIBLE
                } catch (e: IndexOutOfBoundsException) {
                    View.INVISIBLE
                }
            }
            binding.three.run {
                root.visibility = try {
                    val three = item[2]
                    nameTextView.text = three.name
                    Picasso.get().load(three.url).into(imageTextView)
                    root.setOnClickListener {
                        nextFragment(three.id)
                    }
                    View.VISIBLE
                } catch (e: IndexOutOfBoundsException) {
                    View.INVISIBLE
                }
            }
        }
    }
}