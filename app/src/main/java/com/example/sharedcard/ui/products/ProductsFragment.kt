package com.example.sharedcard.ui.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.databinding.FragmentProductsBinding
import com.example.sharedcard.databinding.ListItemProductBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.util.appComponent
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ProductsFragment : Fragment() {

    companion object {
        private const val ID_KEY = "id"
        fun newInstance(id: Int): ProductsFragment {
            val args = Bundle()
            args.putInt(ID_KEY, id)
            val fragment = ProductsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentProductsBinding

    @Inject
    lateinit var factory: ProductsViewModel.FactoryHelper
    private val viewModel by viewModels<ProductsViewModel> {
        factory.create(requireArguments().getInt(ID_KEY, 1))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_products, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        setToolbar()
        return binding.root
    }

    private fun setToolbar() {
        (requireActivity() as NavigationDrawerActivity).setSupportActionBar(binding.appBar.toolbar)
        (requireActivity() as NavigationDrawerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts().observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = ProductAdapter(it)
        }
        viewModel.getCategory().observe(viewLifecycleOwner) {
            (requireActivity() as NavigationDrawerActivity).supportActionBar!!.title = it.name
//            binding.appBar.toolbar.title = it.name

        }
    }

    inner class ProductViewHolder(private val binding: ListItemProductBinding) :
        ViewHolder(binding.root) {

        fun onBind(item: ProductEntity) {
            binding.run {
                Picasso.get().load(item.url).into(binding.picImageView)
                nameTextView.text = item.name
                caloriesTextView.text =
                    getString(R.string.pie_chart_calories, item.calories!!.toInt().toString())
                pfcLineChart.apply {
                    setDataChart(
                        item.protein!!.toFloat(),
                        item.fat!!.toFloat(),
                        item.carb!!.toFloat()
                    )
                    addOnClickListener { color, name ->
                        pointCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                color
                            )
                        )
                        pfcTextView.text = name
                    }
                    startAnimation()
                }
            }
        }
    }

    inner class ProductAdapter(private val list: List<ProductEntity>) :
        RecyclerView.Adapter<ProductViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_product,
                parent,
                false
            )
        )


        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.onBind(list[position])
        }

    }
}