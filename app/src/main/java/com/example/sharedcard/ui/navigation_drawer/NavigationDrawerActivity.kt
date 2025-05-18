package com.example.sharedcard.ui.navigation_drawer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sharedcard.R
import com.example.sharedcard.work_manager.PopularProductWorker
import com.example.sharedcard.databinding.ActivityNavigationDrawerBinding
import com.example.sharedcard.notification.NotificationHelper
import com.example.sharedcard.repository.StompConnectionManager
import com.example.sharedcard.ui.profile.ProfileActivity
import com.example.sharedcard.ui.purchase.AddButtonFragment
import com.example.sharedcard.ui.purchase.PurchaseFragment
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isVisible
import com.example.sharedcard.work_manager.NotificationWorker
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class NavigationDrawerActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityNavigationDrawerBinding
    private lateinit var userTextView: TextView
    private lateinit var userImageView: ImageView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val viewModel: NavigationDrawerViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    private val handler = Handler(Looper.getMainLooper())
    private val connectionHandler = Runnable {
        viewModel.connect()
    }
    private val closeStateLayoutHandler = Runnable {
        binding.stateLayout.isVisible(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        val header = binding.navView.getHeaderView(0)
        userTextView = header.findViewById(R.id.nav_view_user_text_view)
        userImageView = header.findViewById(R.id.nav_view_user_image_view)
        initDrawerToggle()
        setContentView(binding.root)

        if (savedInstanceState == null) {
            initialFragment()
            val constraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val worker = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "notifications",
                ExistingPeriodicWorkPolicy.UPDATE,
                worker
            )
        }

        viewModel.getPerson().observe(this) { user ->
            userTextView.text = user.name
            Picasso.get().load(user.url).placeholder(R.drawable.default_person).into(userImageView)
        }
        viewModel.connectingState.observeForever(::stompStateObserver)

        viewModel.getCountPurchase().observe(this) { count ->
            if (viewModel.countPurchase != count) {
                viewModel.countPurchase = count
                WorkManager.getInstance(this).enqueue(
                    OneTimeWorkRequestBuilder<PopularProductWorker>().build()
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            navView.getHeaderView(0).setOnClickListener {
                startActivity(Intent(baseContext, ProfileActivity::class.java))
            }
            exitButton.setOnClickListener {
                viewModel.exit()
                startActivity(
                    Intent(baseContext, StartupActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            navView.setNavigationItemSelectedListener(this@NavigationDrawerActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.connectingState.removeObserver(::stompStateObserver)

        handler.removeCallbacks(closeStateLayoutHandler)
        handler.removeCallbacks(connectionHandler)
    }


    private fun initDrawerToggle() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.root.findViewById(R.id.toolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun stompStateObserver(state: StompConnectionManager.ConnectionState) {
        when (state) {
            is StompConnectionManager.ConnectionState.CONNECTING -> stateConnecting()
            is StompConnectionManager.ConnectionState.ERROR -> stateError(state.e)
            is StompConnectionManager.ConnectionState.DISCONNECTION -> stateDisconnected()
            is StompConnectionManager.ConnectionState.NEW_PURCHASE -> stateNewPurchase(state)
        }
    }

    private fun stateNewPurchase(state: StompConnectionManager.ConnectionState.NEW_PURCHASE) {
        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            viewModel.createNotification(state.purchases) { items ->
                NotificationHelper.showUserProductNotification(this, items)
            }
        }
    }

    private fun stateError(e: Exception) {
        binding.stateLayout.isVisible(true)
        binding.progressBar.isVisible(false)
        binding.stateLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        val isSocketTimeoutException =
            e.message?.contains(SocketTimeoutException::class.simpleName ?: "") ?: false
        binding.stateTextView.text = if (isSocketTimeoutException) {
            getString(R.string.not_internet)
        } else {
            e.message

        }

    }

    private fun stateDisconnected() {
        if (binding.progressBar.visibility == View.VISIBLE) {
            binding.progressBar.isVisible(false)
            binding.stateTextView.text = getString(R.string.not_internet)
        }
        handler.postDelayed(connectionHandler, 1500)
    }

    private fun stateConnecting() {
        if (binding.stateLayout.visibility == View.VISIBLE) {
            binding.progressBar.isVisible(false)
            binding.stateLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            binding.stateTextView.text = getString(R.string.connection_ok)
            handler.postDelayed(closeStateLayoutHandler, 1000)
        }

    }

    private fun initialFragment() {
        supportFragmentManager.apply {
            commit {
                replace(
                    R.id.background_fragment,
                    PurchaseFragment(),
                    PurchaseFragment::class.simpleName
                )
            }
            commit {
                replace(
                    R.id.nav_host_fragment,
                    AddButtonFragment(),
                    AddButtonFragment::class.simpleName
                )
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        supportFragmentManager.commit {
            setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit)
            replace(R.id.nav_host_fragment, viewModel.fragments[item.itemId]!!)
            addToBackStack(null)
        }
        handler.postDelayed(
            { binding.drawerLayout.closeDrawer(GravityCompat.START) }, 50
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}