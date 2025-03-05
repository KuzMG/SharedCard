package com.example.sharedcard.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.database.entity.recipe.RecipeWithProducts
import com.example.sharedcard.repository.DictionaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RecipeDetailsViewModel(
    private val idRecipe: Int,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {


    fun getRecipe(): LiveData<RecipeWithProducts> = dictionaryRepository.getRecipe(idRecipe)



    class ViewModelFactory @AssistedInject constructor(
        @Assisted("idCategory") private val idRecipe: Int,
        private val dictionaryRepository: DictionaryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == RecipeDetailsViewModel::class.java)
            return RecipeDetailsViewModel(idRecipe, dictionaryRepository) as T
        }
    }
    @AssistedFactory
    interface FactoryHelper{
        fun create(@Assisted("idCategory") idRecipe: Int): ViewModelFactory
    }
}