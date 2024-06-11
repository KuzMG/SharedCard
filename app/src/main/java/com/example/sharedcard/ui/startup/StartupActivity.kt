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
import com.example.sharedcard.ui.startup.authorization.AuthorizationFragment
import com.example.sharedcard.ui.startup.registration.RegistrationFragment
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
                    AuthorizationFragment(),
                    AuthorizationFragment::class.simpleName!!
                )

                Continue -> startActivity(
                    Intent(this, NavigationDrawerActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )

                else -> throw IndexOutOfBoundsException()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (currentFragment != StartupFragment::class.simpleName) {
            viewModel.setTransitionState(Startup)
        } else {
            super.onBackPressed()
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