package com.example.sharedcard.di

import android.app.Application
import com.example.sharedcard.work_manager.PopularProductWorker
import com.example.sharedcard.database.entity.category_product.CategoryProductDao
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.shop.ShopDao
import com.example.sharedcard.di.module.DBModule
import com.example.sharedcard.di.module.ServiceModule
import com.example.sharedcard.di.module.ViewModelModule
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.ui.purchase.to_history.ToHistoryBottomSheet
import com.example.sharedcard.ui.group.edit_group.EditGroupBottomSheet
import com.example.sharedcard.ui.products.ProductsFragment
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseFragment
import com.example.sharedcard.ui.recipes.RecipeDetailsFragment
import com.example.sharedcard.ui.recipes.RecipesFragment
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import com.example.sharedcard.work_manager.NotificationWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DBModule::class, ServiceModule::class, ViewModelModule::class])
interface AppComponent {
    val accountManager: AccountManager
    val multiViewModelFactory: MultiViewModelFactory
    val currencyDao: CurrencyDao
    val metricDao: MetricDao
    val productDao: ProductDao
    val categoryDao: CategoryProductDao
    val shopDao: ShopDao
    fun inject(fragment: ToHistoryBottomSheet)
    fun inject(fragment: EditGroupBottomSheet)
    fun inject(fragment: ProductsFragment)
    fun inject(fragment: RecipesFragment)
    fun inject(fragment: RecipeDetailsFragment)
    fun inject(fragment: AddPurchaseFragment)
    fun inject(popularProductWorker: PopularProductWorker)
    fun inject(notificationWorker: NotificationWorker)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}