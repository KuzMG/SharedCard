package com.example.sharedcard.ui.navigation_drawer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sharedcard.R
import com.example.sharedcard.databinding.ActivityNavigationDrawerBinding
import com.example.sharedcard.background.SynchronizationWorker
import com.example.sharedcard.ui.purchase.AddButtonFragment
import com.example.sharedcard.ui.purchase.PurchaseFragment
import com.example.sharedcard.ui.profile.ProfileActivity
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.util.appComponent
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso


class NavigationDrawerActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {


    private lateinit var binding: ActivityNavigationDrawerBinding


    private val viewModel: NavigationDrawerViewModel by viewModels {
        appComponent.multiViewModelFactory
    }
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            synchronizationWork()
            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment,AddButtonFragment(),AddButtonFragment::class.simpleName)
            }
        }
        binding.navView.setNavigationItemSelectedListener(this)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.root.findViewById(R.id.toolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        initialFragment()

        viewModel.getPerson().observe(this) { user ->
            val header = binding.navView.getHeaderView(0)
            val userTextView =   header.findViewById<TextView>(R.id.nav_view_user_text_view)
            val userImageView =   header.findViewById<ImageView>(R.id.nav_view_user_image_view)

            userTextView?.text = user.name
            userImageView?.let {
                Picasso.get()
                    .load(user.url)
                    .into(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            navView.getHeaderView(0).setOnClickListener {
                startActivity(Intent(this@NavigationDrawerActivity, ProfileActivity::class.java))
            }
            exitButton.setOnClickListener {
                viewModel.exit()
                startActivity(
                    Intent(this@NavigationDrawerActivity, StartupActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

    }

    private fun initialFragment() {
        supportFragmentManager.commit {
            replace(R.id.background_fragment, PurchaseFragment(), PurchaseFragment::class.simpleName)
        }
    }

    private fun synchronizationWork() {
        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<SynchronizationWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        supportFragmentManager.commit {
            setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit)
            replace(R.id.nav_host_fragment, viewModel.fragments[item.itemId]!!)
            addToBackStack(null)
        }
        Handler(Looper.getMainLooper()).postDelayed(
            { binding.drawerLayout.closeDrawer(GravityCompat.START) },
            50
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}
