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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.profile)

        viewModel.getUser().observe(this) { user ->
            binding.nameTextView.text = user.name
            binding.emailTextView.text = user.email
            binding.heightTextView.text = getString(R.string.height, user.height.toString())
            binding.weightTextView.text = getString(R.string.weight, user.weight.toString())
            binding.ageTextView.text = getString(R.string.age, user.age.toString())
            binding.profileImageView
//            Picasso.get().apply {
//                if(load("").networkPolicy(NetworkPolicy.OFFLINE).get() == null){
//                    load("").into(binding.profileImageView)
//                } else{
//                    load("").networkPolicy(NetworkPolicy.OFFLINE).into(binding.profileImageView)
//                }
//            }
        }
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