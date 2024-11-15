package com.example.sharedcard.ui.recipes

import androidx.lifecycle.ViewModel
import com.example.sharedcard.repository.DictionaryRepository
import javax.inject.Inject

class RecipeCategoriesViewModel @Inject constructor(private val repository: DictionaryRepository) :
    ViewModel() {
    fun getRecipeCategories() =
        repository.getAllCategoriesRecipe()

}