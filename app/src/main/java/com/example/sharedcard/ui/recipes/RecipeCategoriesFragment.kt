package com.example.sharedcard.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.databinding.FragmentRecipeCategoriesBinding
import com.example.sharedcard.ui.products.adapters.CategoryAdapter
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel
import com.example.sharedcard.util.appComponent

class RecipeCategoriesFragment : Fragment() {


    private val viewModel: RecipeCategoriesViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    private val navigationDrawerViewModel: NavigationDrawerViewModel by viewModels(  {activity as NavigationDrawerActivity }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentRecipeCategoriesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeCategoriesBinding.inflate(
            inflater,
            container,
            false
        )
        setToolbar()
        return binding.root
    }
    private fun setToolbar() {
        (requireActivity() as NavigationDrawerActivity).setSupportActionBar(binding.appBar.toolbar)
        (requireActivity() as NavigationDrawerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
        (requireActivity() as NavigationDrawerActivity).supportActionBar?.setTitle(R.string.recipes)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRecipeCategories().observe(viewLifecycleOwner) { categories ->
            val list = arrayListOf<ArrayList<CategoryProductEntity>>()
            categories.forEachIndexed { index, category ->
                if(index/4 == 0){
                    if(index/2 >= list.size){
                        list.add(arrayListOf())
                    }
                    list[index/2].add(category)
                } else{
                    if((index-4)/3 >= list.size-2){
                        list.add(arrayListOf())
                    }
                    list[(index-4)/3+2].add(category)
                }
            }
            binding.recyclerView.adapter = CategoryAdapter(list){
                parentFragmentManager.commit {
                    setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit)
                    replace(R.id.nav_host_fragment, RecipesFragment.newInstance(it))
                    addToBackStack(null)
                }
            }
        }
    }
}