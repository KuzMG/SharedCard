package com.example.sharedcard.ui.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sharedcard.R
import com.example.sharedcard.databinding.ActivityProfileBinding
import com.example.sharedcard.util.appComponent

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        setToolbar()

        viewModel.getUser().observe(this) { user ->
            binding.run {
                nameTextView.text = user.name
                heightTextView.text = getString(R.string.height, user.height.toString())
                weightTextView.text = getString(R.string.weight, user.weight.toString())
                ageTextView.text = getString(R.string.age, user.age.toString())
            }
        }
        viewModel.getUserAccount().observe(this) { userAccount ->
            binding.emailTextView.text = userAccount.email
        }
    }

    private fun setToolbar() {
        binding.appBar.toolbar.setTitle(R.string.profile)
        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            nameLayout.setOnClickListener {
                EditTextBottomSheet()
                    .show(supportFragmentManager, EditTextBottomSheet.TAG)
            }
            emailLayout.setOnClickListener {
            }
            weightLayout.setOnClickListener {
                EditNumberBottomSheet.run {
                    newInstance(WEIGHT)
                        .show(supportFragmentManager, TAG)
                }
            }
            heightLayout.setOnClickListener {
                EditNumberBottomSheet.run {
                    newInstance(HEIGHT)
                        .show(supportFragmentManager, TAG)
                }
            }
            ageLayout.setOnClickListener {
                EditNumberBottomSheet.run {
                    newInstance(AGE)
                        .show(supportFragmentManager, TAG)
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressedDispatcher.onBackPressed()
            true
        }

        else -> false
    }

}