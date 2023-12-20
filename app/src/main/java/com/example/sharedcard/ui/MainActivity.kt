package com.example.sharedcard.ui

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sharedcard.R
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.databinding.ActivityMainBinding
import com.example.sharedcard.ui.authorization.AuthorizationFragment
import com.example.sharedcard.ui.bottom_navigation.NavigationFragment
import com.example.sharedcard.ui.change_password.ChangePasswordFragment
import com.example.sharedcard.ui.registration.RegistrationFragment

class MainActivity : AppCompatActivity(), AuthorizationFragment.Callbacks, RegistrationFragment.Callbacks , ChangePasswordFragment.Callbacks, NavigationFragment.Callbacks{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.nav_graph_main)

        val userId = (application as SharedCardApp).getQueryPreferences().userId
        if (userId == 0L) {
            graph.setStartDestination(R.id.authorizationFragment)
        } else {
            graph.setStartDestination(R.id.navigationFragment)
        }
        navController.graph = graph

    }

    fun setToolbar(@StringRes resId: Int) {
        supportActionBar?.title = getString(resId)
    }

    override fun onAuthorizationToRegistrationFragment() {
        navController.navigate(R.id.action_authorizationFragment_to_registrationFragment)
    }

    override fun onAuthorizationToNavigationFragment() {
        navController.navigate(R.id.action_authorizationFragment_to_navigationFragment)
    }

    override fun onAuthorizationToChangePasswordFragment() {
        navController.navigate(R.id.action_authorizationFragment_to_changePasswordFragment)
    }

    override fun onRegistrationToNavigationFragment() {
        navController.navigate(R.id.action_registrationFragment_to_navigationFragment)
    }

    override fun onChangePasswordToAuthorizationFragment() {
        navController.popBackStack()
    }

    override fun onNavigationToSettingsFragment() {
        navController.navigate(R.id.action_navigationFragment_to_settingsFragment)
    }
}