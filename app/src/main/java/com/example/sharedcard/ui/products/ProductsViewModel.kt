package com.example.sharedcard.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.repository.DictionaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProductsViewModel(
    private val idCategory: Int,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {


    fun getProducts() = dictionaryRepository.getProductByCategory(idCategory)

    fun getCategory(): LiveData<CategoryProductEntity> = dictionaryRepository.getCategoryProductLiveData(idCategory)

    class ViewModelFactory @AssistedInject constructor(
        @Assisted("idCategory") private val idCategory: Int,
        private val dictionaryRepository: DictionaryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ProductsViewModel::class.java)
            return ProductsViewModel(idCategory, dictionaryRepository) as T
        }
    }
    @AssistedFactory
    interface FactoryHelper {
        fun create(@Assisted("idCategory") idCategory: Int): ViewModelFactory
    }
}