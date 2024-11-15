package com.example.sharedcard.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.databinding.FragmentProductCategoriesBinding
import com.example.sharedcard.databinding.FragmentRecipeCategoriesBinding
import com.example.sharedcard.ui.adapter.CategoryAdapter
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_recipe_categories,
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
            val list = arrayListOf<ArrayList<CategoryEntity>>()
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
                navigationDrawerViewModel.setTransitionState(NavigationDrawerViewModel.State.Recipes,RecipesFragment.newInstance(it))
            }
        }
    }
}