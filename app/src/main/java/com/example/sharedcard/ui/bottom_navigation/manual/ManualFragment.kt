package com.example.sharedcard.ui.bottom_navigation.manual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.databinding.FragmentManualBinding
import com.example.sharedcard.databinding.ListItemCategoryBinding
import com.example.sharedcard.ui.bottom_navigation.manual.product_list.ProductListFragment
import com.squareup.picasso.Picasso

class ManualFragment : Fragment() {


    private val viewModel: ManualViewModel by viewModels()
    private lateinit var binding: FragmentManualBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_manual,
            container,
            false
        )
        binding.listCategoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategories().observe(viewLifecycleOwner) { categories ->
            binding.listCategoryRecyclerView.adapter = CategoryAdapter(categories)
        }
    }

    private inner class CategoryViewHolder(private val binding: ListItemCategoryBinding) :
        ViewHolder(binding.root), View.OnClickListener {
        private lateinit var category: CategoryEntity
        init {
            itemView.setOnClickListener(this)
        }
        fun onBind(category: CategoryEntity) {
            this.category = category
            binding.nameTextView.text = category.name
            val sr = "http://192.168.80.31:8080/category/photo/${category.id}"
            Picasso.get()
                .load(sr)
                .into(binding.categoryImageView)
        }

        override fun onClick(p0: View?) {
            findNavController().navigate(
                R.id.action_manualFragment_to_productListFragment,
                ProductListFragment.getBundle(category.id)
            )
        }

    }

    private inner class CategoryAdapter(private val categories: List<CategoryEntity>) :
        RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_category,
                parent,
                false
            )
        )

        override fun getItemCount() = categories.size

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.onBind(categories[position])
        }
    }
}