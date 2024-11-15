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
import androidx.databinding.DataBindingUtil
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sharedcard.R
import com.example.sharedcard.databinding.ActivityNavigationDrawerBinding
import com.example.sharedcard.background.SynchronizationWorker
import com.example.sharedcard.ui.check.CheckFragment
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.CategoryProducts
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.Group
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.History
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.Products
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.RecipeProducts
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.Recipes
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.Settings
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.Start
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel.State.Statistic
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
        if(savedInstanceState == null){
            synchronizationWork()
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation_drawer)

        binding.appBar.toolbar.setTitle(R.string.check)
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

        viewModel.getGroup().observe(this) {group ->
            group ?: return@observe
            binding.appBar.toolbar.setTitle(group.name)
        }
        viewModel.getUser().observe(this) { user ->
            binding.navView.findViewById<TextView>(R.id.nav_view_user_text_view).text = user.name
            Picasso.get()
                .load(user.url)
                .into(binding.navView.findViewById<ImageView>(R.id.nav_view_user_image_view))
        }



        viewModel.transitionState.observe(this) { state ->
            if (viewModel.fragmentStack.size == 0 || viewModel.fragmentStack.peek() != state) {
                val fm =
                    supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit)
                        .replace(R.id.nav_host_fragment, viewModel.fragments[state]!!)
                if (state == Start) {
                    fm.commit()
                } else {
                    fm.addToBackStack(null).commit()
                }
                viewModel.fragmentStack.push(state)
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.background_fragment, CheckFragment(), CheckFragment::class.simpleName)
            .commit()
        if (viewModel.fragmentStack.size == 0)
            viewModel.setTransitionState(Start)
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
        when (item.itemId) {
            R.id.products -> viewModel.setTransitionState(CategoryProducts)
            R.id.recipes -> viewModel.setTransitionState(RecipeProducts)
            R.id.statistic -> viewModel.setTransitionState(Statistic)
            R.id.group -> viewModel.setTransitionState(Group)
            R.id.settings -> viewModel.setTransitionState(Settings)
            R.id.history -> viewModel.setTransitionState(History)
            else -> throw IndexOutOfBoundsException()
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
        viewModel.fragmentStack.pop()
        if (viewModel.fragmentStack.size > 0)
            viewModel.setTransitionState(viewModel.fragmentStack.peek())
    }
}
