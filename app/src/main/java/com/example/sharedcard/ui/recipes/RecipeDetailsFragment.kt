package com.example.sharedcard.ui.recipes

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.recipe.RecipeProduct
import com.example.sharedcard.databinding.FragmentRecipeDetailsBinding
import com.example.sharedcard.databinding.ListItemIngredientBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.util.appComponent
import com.squareup.picasso.Picasso
import javax.inject.Inject

class RecipeDetailsFragment : Fragment() {

    companion object {
        private const val ID_KEY = "id"
        fun newInstance(id: Int): RecipeDetailsFragment {
            val args = Bundle()
            args.putInt(ID_KEY, id)
            val fragment = RecipeDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var factory: RecipeDetailsViewModel.FactoryHelper
    private val viewModel by viewModels<RecipeDetailsViewModel> {
        factory.create(requireArguments().getInt(ID_KEY))
    }
    private lateinit var binding: FragmentRecipeDetailsBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = ChangeBounds()

        // Анимации при закрытии фрагмента
        // Если не указать, будет использована sharedElementEnterTransition
        sharedElementReturnTransition = ChangeBounds()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_recipe_details,
            container,
            false
        )
        ViewCompat.setTransitionName(binding.materialCardView, "recipe_details_card_view")
        ViewCompat.setTransitionName(binding.nameTextView, "recipe_details_name_text_view")
        setToolbar()
        return binding.root
    }
    private fun setToolbar() {
        (requireActivity() as NavigationDrawerActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRecipe().observe(viewLifecycleOwner) {
            binding.apply {
                nameTextView.text = it.recipe.name
                toolbar.setNavigationOnClickListener {
                    parentFragmentManager.popBackStack()
                }
                toolbar.inflateMenu(R.menu.detail_recipes_menu)
                portionTextView.text = it.recipe.portion.toString()
                caloriesTextView.text =
                    getString(R.string.pie_chart_calories, it.recipe.calories.toString())
                Picasso.get().load(it.recipe.url).into(picImageView)
                pfcLineChart.apply {
                    setDataChart(
                        it.recipe.protein!!.toFloat(),
                        it.recipe.fat!!.toFloat(),
                        it.recipe.carb!!.toFloat()
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

                ingredientsRecyclerView.adapter = Adapter(it.products)

                descriptionTextView.text = it.recipe.description
            }
        }
    }

    private inner class ViewHolder(private val listItemIngredientBinding: ListItemIngredientBinding) :
        RecyclerView.ViewHolder(listItemIngredientBinding.root) {
        fun bind(item: RecipeProduct) {
            listItemIngredientBinding.apply {
                nameTextView.text = item.product.name
                Picasso.get().load(item.product.url).into(picImageView)
                countTextView.text = item.count.toString()
                metricTextView.text = item.metric.name
            }
        }
    }

    private inner class Adapter(private val list: List<RecipeProduct>) :
        RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder (DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_ingredient,
                parent,
                false
            ))


        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

    }
}