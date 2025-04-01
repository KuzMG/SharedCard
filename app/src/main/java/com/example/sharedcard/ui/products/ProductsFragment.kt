package com.example.sharedcard.ui.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentProductsBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.products.adapters.ProductAdapter
import com.example.sharedcard.ui.purchase.AddButtonFragment
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseFragment
import com.example.sharedcard.util.appComponent
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
            FragmentProductsBinding.inflate(layoutInflater, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts().observe(viewLifecycleOwner) {products ->
            binding.recyclerView.adapter = ProductAdapter(products){ product ->
                parentFragmentManager.commit {
                    parentFragmentManager.popBackStack()
                    parentFragmentManager.popBackStack()
                    replace(R.id.nav_host_fragment,AddPurchaseFragment.newInstance(product.productEntity.name))
                    addToBackStack(null)
                }
            }
        }
        viewModel.getCategory().observe(viewLifecycleOwner) {
            binding.appBar.toolbar.title = it.name
        }
    }

    override fun onStart() {
        super.onStart()
        binding.appBar.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}