package com.example.sharedcard.ui.startup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sharedcard.R
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.startup.StartupViewModel.State.Authorization
import com.example.sharedcard.ui.startup.StartupViewModel.State.Continue
import com.example.sharedcard.ui.startup.StartupViewModel.State.Registration
import com.example.sharedcard.ui.startup.StartupViewModel.State.Startup
import com.example.sharedcard.ui.startup.StartupViewModel.State.Synchronization
import com.example.sharedcard.ui.startup.authentication.AuthenticationFragment
import com.example.sharedcard.ui.startup.registration.RegistrationFragment
import com.example.sharedcard.ui.startup.synchronization.SyncFragment
import com.example.sharedcard.util.appComponent


class StartupActivity : AppCompatActivity() {


    private val viewModel: StartupViewModel by viewModels {
        appComponent.multiViewModelFactory
    }
    private var currentFragment: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        viewModel.transitionState.observe(this) {
            when (it) {
                Startup -> showFragment(StartupFragment(), StartupFragment::class.simpleName!!)
                Registration -> showFragment(
                    RegistrationFragment(),
                    RegistrationFragment::class.simpleName!!
                )

                Authorization -> showFragment(
                    AuthenticationFragment(),
                    AuthenticationFragment::class.simpleName!!
                )

                Synchronization -> showFragment(
                    SyncFragment(),
                    SyncFragment::class.simpleName!!
                )

                Continue -> startActivity(
                    Intent(this, NavigationDrawerActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )

                else -> throw IndexOutOfBoundsException()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when(currentFragment){
            StartupFragment::class.simpleName ->  super.onBackPressed()
            RegistrationFragment::class.simpleName -> {
                if(viewModel.currentPageReg>1){
                    viewModel.currentPageReg--
                } else {
                    viewModel.setTransitionState(Startup)
                }
            }
            else -> viewModel.setTransitionState(Startup)
        }
    }

    private fun showFragment(f: Fragment, name: String) {
        if (currentFragment == name)
            return
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.nav_host_fragment_startup,
                f,
                name
            )
            .commit()
        currentFragment = name
    }

}