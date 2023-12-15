package com.example.sharedcard.ui.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentNavigationBinding
import com.example.sharedcard.ui.MainActivity
import com.example.sharedcard.ui.bottom_navigation.check.CheckFragment

class NavigationFragment : Fragment() {
    private lateinit var binding: FragmentNavigationBinding

    private lateinit var viewModel: NavigationViewModel
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


        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_bottom_navigation) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        return binding.root
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