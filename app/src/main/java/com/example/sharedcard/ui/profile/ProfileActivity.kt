package com.example.sharedcard.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedcard.R
import com.example.sharedcard.databinding.ActivityProfileBinding
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        appComponent.multiViewModelFactory
    }
    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stream = contentResolver.openInputStream(result.data?.data!!)
                val bitmap = BitmapFactory.decodeStream(stream)
                viewModel.setImage(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()

        viewModel.getUser().observe(this) { user ->
            binding.run {
                Picasso.get()
                    .load(user.url)
                    .into(profileImageView)
                nameTextView.text = user.name
                heightTextView.text = getString(R.string.height, user.height.toString())
                weightTextView.text = getString(R.string.weight, user.weight.toString())
                val format = SimpleDateFormat(getString(R.string.date_format))
                ageTextView.text = format.format(user.birthday)
            }
        }
        viewModel.getUserAccount().observe(this) { userAccount ->
            binding.emailTextView.text = userAccount.email
        }
        viewModel.loadingLiveData.observe(this) { result ->
            when (result.state) {
                Result.State.OK -> loading(false)
                Result.State.ERROR -> loading(false)
                Result.State.LOADING -> loading(true)
            }
            result.error?.let {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loading(flag: Boolean) {
        binding.run {
            progressIndicator.visibility = isVisible(flag)
        }
    }

    private fun isVisible(flag: Boolean) =
        if (flag) View.VISIBLE else View.GONE

    private fun setToolbar() {
        binding.appBar.toolbar.setTitle(R.string.profile)
        binding.appBar.toolbar.setNavigationIcon(R.drawable.ic_arrow_left)
        binding.appBar.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            profileImageView.setOnClickListener {
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"
                captureImageLauncher.launch(pickIntent)
            }
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
                showDatePickerDialog { datePicker, i, i2, i3 ->
                    val calendar =
                        GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                    viewModel.setDate(calendar.time.time)
                }
            }
        }

    }
    private fun showDatePickerDialog(click: DatePickerDialog.OnDateSetListener) {
        val calendar = GregorianCalendar()
        val year = calendar.get(GregorianCalendar.YEAR)
        val month = calendar.get(GregorianCalendar.MONTH)
        val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
        DatePickerDialog(
            this, R.style.DialogTheme,
            click,
            year,
            month,
            day
        ).show()
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressedDispatcher.onBackPressed()
            true
        }

        else -> false
    }

}