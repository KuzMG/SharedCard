package com.example.sharedcard.ui.navigation_drawer

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.ui.check.AddButtonFragment
import com.example.sharedcard.ui.group.GroupFragment
import com.example.sharedcard.ui.history.HistoryFragment
import com.example.sharedcard.ui.products.ProductCategoriesFragment
import com.example.sharedcard.ui.products.ProductsFragment
import com.example.sharedcard.ui.recipes.RecipeCategoriesFragment
import com.example.sharedcard.ui.recipes.RecipesFragment
import com.example.sharedcard.ui.settings.SettingsFragment
import com.example.sharedcard.ui.statistic.StatisticFragment
import java.util.Stack
import javax.inject.Inject

class NavigationDrawerViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val groupManager: GroupManager
) :
    ViewModel() {
    enum class State {
        Start, Group, Statistic, Settings, CategoryProducts, RecipeProducts,Recipes, History, Products
    }

    val fragmentStack: Stack<State> = Stack()
    val fragments: MutableMap<State, Fragment> =
        mutableMapOf(
            State.Start to AddButtonFragment(),
            State.Group to GroupFragment(),
            State.Statistic to StatisticFragment(),
            State.Settings to SettingsFragment(),
            State.CategoryProducts to ProductCategoriesFragment(),
            State.RecipeProducts to RecipeCategoriesFragment(),
            State.History to HistoryFragment()
        )
    private val _transition = MutableLiveData<State>()
    val transitionState: LiveData<State> = _transition

    fun getUser() = accountManager.getUser()

    fun getGroup() = groupManager.getCurrentGroup()
    fun setTransitionState(state: State,fragment: Fragment? = null) {
        fragment?.let {
            fragments[state] = fragment
        }
        _transition.value = state
    }

    fun exit() {
        accountManager.exitFromAccount()
    }
}