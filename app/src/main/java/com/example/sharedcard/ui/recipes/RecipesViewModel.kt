package com.example.sharedcard.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.database.entity.recipe.Recipe
import com.example.sharedcard.repository.DictionaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RecipesViewModel(
    private val idCategory: Int,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    fun getRecipes() : LiveData<List<Recipe>> = dictionaryRepository.getRecipesByCategory(idCategory)
    fun getCategory() = dictionaryRepository.getCategoryProduct(idCategory)



    class ViewModelFactory @AssistedInject constructor(
        @Assisted("idCategory") private val idCategory: Int,
        private val dictionaryRepository: DictionaryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == RecipesViewModel::class.java)
            return RecipesViewModel(idCategory, dictionaryRepository) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(@Assisted("idCategory") idCategory: Int): ViewModelFactory
    }
}