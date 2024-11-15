package com.example.sharedcard.di.module

import androidx.lifecycle.ViewModel
import com.example.sharedcard.ui.check.add_check.AddCheckViewModel
import com.example.sharedcard.ui.check.add_target.AddTargetViewModel
import com.example.sharedcard.ui.check.product.ProductViewModel
import com.example.sharedcard.ui.check.target.TargetViewModel
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.ui.group.create_group.CreateGroupViewModel
import com.example.sharedcard.ui.group.token_group.DialogTokenViewModel
import com.example.sharedcard.ui.group.user_group.UserGroupViewModel
import com.example.sharedcard.ui.history.product.HistoryProductViewModel
import com.example.sharedcard.ui.history.target.HistoryTargetViewModel
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel
import com.example.sharedcard.ui.products.ProductCategoriesViewModel
import com.example.sharedcard.ui.products.ProductsViewModel
import com.example.sharedcard.ui.profile.ProfileViewModel
import com.example.sharedcard.ui.recipes.RecipeCategoriesViewModel
import com.example.sharedcard.ui.recipes.RecipesViewModel
import com.example.sharedcard.ui.settings.SettingsViewModel
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.ui.statistic.StatisticViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
interface ViewModelModule {
    @[Binds IntoMap ViewModelKey(AddCheckViewModel::class)]
    fun provideAddCheckViewModel(addCheckViewModel: AddCheckViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(AddTargetViewModel::class)]
    fun provideAddTargetViewModel(addTargetViewModel: AddTargetViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(ProductViewModel::class)]
    fun provideProductViewModel(productViewModel: ProductViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(TargetViewModel::class)]
    fun provideTargetViewModel(targetViewModel: TargetViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(CreateGroupViewModel::class)]
    fun provideCreateGroupViewModel(createGroupViewModel: CreateGroupViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(ProductCategoriesViewModel::class)]
    fun provideProductCategoriesViewModel(productCategoriesViewModel: ProductCategoriesViewModel): ViewModel


    @[Binds IntoMap ViewModelKey(DialogTokenViewModel::class)]
    fun provideDialogTokenViewModel(dialogTokenViewModel: DialogTokenViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(UserGroupViewModel::class)]
    fun provideUserGroupViewModel(userGroupViewModel: UserGroupViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(GroupViewModel::class)]
    fun provideGroupViewModel(groupViewModel: GroupViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(HistoryProductViewModel::class)]
    fun provideHistoryProductViewModel(historyProductViewModel: HistoryProductViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(HistoryTargetViewModel::class)]
    fun provideHistoryTargetViewModel(historyTargetViewModel: HistoryTargetViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(NavigationDrawerViewModel::class)]
    fun provideNavigationDrawerViewModel(navigationDrawerViewModel: NavigationDrawerViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(ProfileViewModel::class)]
    fun provideProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(RecipeCategoriesViewModel::class)]
    fun provideRecipeCategoriesViewModel(recipeCategoriesViewModel: RecipeCategoriesViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(SettingsViewModel::class)]
    fun provideSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(StartupViewModel::class)]
    fun provideStartupViewModel(startupViewModel: StartupViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(StatisticViewModel::class)]
    fun provideStatisticViewModel(statisticViewModel: StatisticViewModel): ViewModel

}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)