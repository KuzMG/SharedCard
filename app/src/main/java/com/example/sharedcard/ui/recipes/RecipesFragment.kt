package com.example.sharedcard.ui.recipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.recipe.Recipe
import com.example.sharedcard.databinding.FragmentProductsBinding
import com.example.sharedcard.databinding.FragmentRecipesBinding
import com.example.sharedcard.databinding.ListItemProductBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel
import com.example.sharedcard.ui.products.ProductsFragment
import com.example.sharedcard.ui.products.ProductsViewModel
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

class RecipesFragment : Fragment() {

    companion object {
        private const val ID_KEY = "id"
        fun newInstance(id: Int): RecipesFragment {
            val args = Bundle()
            args.putInt(ID_KEY, id)
            val fragment = RecipesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var factory: RecipesViewModel.FactoryHelper
    private val viewModel by viewModels<RecipesViewModel> {
        factory.create(requireArguments().getInt(ID_KEY))
    }
    private lateinit var binding: FragmentRecipesBinding
    private val navigationDrawerViewModel: NavigationDrawerViewModel by viewModels({ activity as NavigationDrawerActivity }) {
        appComponent.multiViewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        setToolbar()
        return binding.root
    }

    private fun setToolbar() {
        (requireActivity() as NavigationDrawerActivity).apply {
            setSupportActionBar(binding.appBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRecipes().observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = RecipeAdapter(it)
        }
        viewModel.getCategory().observe(viewLifecycleOwner) {
            (requireActivity() as NavigationDrawerActivity).supportActionBar?.title = it.name
        }
    }

    inner class RecipeViewHolder(private val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun onBind(item: Recipe) {
            binding.run {
                root.setOnClickListener {
                    ViewCompat.setTransitionName(binding.materialCardView, "recipe_card_view")
                    ViewCompat.setTransitionName(binding.nameTextView, "recipe_name_text_view")
                    parentFragmentManager.commit {
                        addSharedElement(binding.materialCardView, "recipe_details_card_view")
                        addSharedElement(binding.nameTextView, "recipe_details_name_text_view")
                        replace(R.id.nav_host_fragment, RecipeDetailsFragment.newInstance(item.id))
                        addToBackStack(null)
                    }
                }
                nameTextView.text = item.name
                Picasso.get().load(item.url).into(binding.picImageView)
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

    inner class RecipeAdapter(private val list: List<Recipe>) :
        RecyclerView.Adapter<RecipeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecipeViewHolder(
            ListItemProductBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )


        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            holder.onBind(list[position])
        }

    }

}