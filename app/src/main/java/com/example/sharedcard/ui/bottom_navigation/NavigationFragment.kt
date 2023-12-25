package com.example.sharedcard.ui.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sharedcard.R
import com.example.sharedcard.background_work.SynchronizationWorker
import com.example.sharedcard.databinding.FragmentNavigationBinding
import com.example.sharedcard.ui.MainActivity
import com.example.sharedcard.ui.bottom_navigation.check.CheckFragment
import com.example.sharedcard.ui.bottom_navigation.group.GroupFragment
import com.example.sharedcard.ui.bottom_navigation.history.HistoryFragment
import com.example.sharedcard.ui.bottom_navigation.manual.ManualFragment
import com.example.sharedcard.ui.bottom_navigation.statistic.StatisticFragment
import com.example.sharedcard.ui.bottom_navigation.statistic.StatisticViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationFragment : Fragment() {
    interface Callbacks {
        fun onNavigationToSettingsFragment()
    }

    private lateinit var binding: FragmentNavigationBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_navigation,
            container,
            false
        )
        binding.bottomNavigation.selectedItemId = R.id.checkFragment


        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_bottom_navigation) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workerInstance()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.settings_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        (requireActivity() as MainActivity).onNavigationToSettingsFragment()
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.CREATED)
    }
    fun workerInstance(){

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val requestWorker = OneTimeWorkRequestBuilder<SynchronizationWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(requestWorker);
    }

    override fun onStart() {
        super.onStart()


        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                when (destination.id) {
                    R.id.manualFragment ->{
                        (requireActivity() as MainActivity).setToolbar(R.string.manual)
                    }
                    R.id.checkFragment ->{
                        (requireActivity() as MainActivity).setToolbar(R.string.check)
                    }
                    R.id.groupFragment ->{
                        (requireActivity() as MainActivity).setToolbar(R.string.group)
                    }
                    R.id.statisticFragment -> {
                        (requireActivity() as MainActivity).setToolbar(R.string.statistic)
                    }
                    R.id.storyFragment -> {
                        (requireActivity() as MainActivity).setToolbar(R.string.history)
                    }
                }
            }

        })
    }


}