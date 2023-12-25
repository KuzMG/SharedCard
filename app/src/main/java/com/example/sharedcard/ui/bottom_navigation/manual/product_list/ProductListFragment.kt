package com.example.sharedcard.ui.bottom_navigation.manual.product_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.databinding.FragmentProductListBinding
import com.example.sharedcard.databinding.ListItemProductBinding
import com.squareup.picasso.Picasso

class ProductListFragment : Fragment() {

    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var binding: FragmentProductListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.idCategory = arguments?.getLong(KEY_ID)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_product_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts().observe(viewLifecycleOwner) { products ->
            binding.recyclerView.adapter = ProductAdapter(products)
        }
    }

    companion object {
        private const val KEY_ID = "idCategory"
        fun getBundle(value: Long) = Bundle().apply {
            putLong(KEY_ID, value)
        }
    }

    private inner class ProductViewHolder(private val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var product: ProductEntity

        init {
            itemView.setOnClickListener(this)
        }

        fun onBind(product: ProductEntity) {
            this.product = product
            binding.apply {
                nameTextView.text = product.name
                pfcTextView.text = "${product.calorie}/${product.protein}/${product.fat}/${product.carb}"
            }
            val sr = "http://192.168.80.31:8080/category/photo/product/${product.id}"
            Picasso.get()
                .load(sr)
                .into(binding.categoryImageView)
        }

        override fun onClick(p0: View?) {

        }

    }

    private inner class ProductAdapter(private val products: List<ProductEntity>) :
        RecyclerView.Adapter<ProductViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_product,
                parent,
                false
            )
        )

        override fun getItemCount() = products.size

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.onBind(products[position])
        }
    }
}