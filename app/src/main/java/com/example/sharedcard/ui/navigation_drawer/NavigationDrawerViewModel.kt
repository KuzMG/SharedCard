package com.example.sharedcard.ui.navigation_drawer

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedcard.R
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.ui.group.GroupFragment
import com.example.sharedcard.ui.history.HistoryFragment
import com.example.sharedcard.ui.products.ProductCategoriesFragment
import com.example.sharedcard.ui.settings.SettingsFragment
import com.example.sharedcard.ui.statistic.StatisticFragment
import java.util.Stack
import javax.inject.Inject

class NavigationDrawerViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val groupManager: GroupManager
) :
    ViewModel() {
    val fragments: MutableMap<Int, Fragment> =
        mutableMapOf(
            R.id.products to ProductCategoriesFragment(),
            R.id.statistic to StatisticFragment(),
            R.id.group to GroupFragment(),
            R.id.settings to SettingsFragment(),
            R.id.history to HistoryFragment(),
        )

    fun getPerson() = accountManager.getPerson()


    fun exit() {
        accountManager.exitFromAccount()
    }
}