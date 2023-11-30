package com.example.sharedcard.ui.registration

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sharedcard.R
import com.example.sharedcard.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()
    private lateinit var binding: ActivityRegistrationBinding

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(contentResolver, uri!!)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                registrationViewModel.savePhoto(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_registration
        )

        binding.imageUser.viewTreeObserver.addOnGlobalLayoutListener {
            registrationViewModel.heightPhotoView = binding.imageUser.height
            registrationViewModel.widthPhotoView = binding.imageUser.width
        }

        registrationViewModel.savePhotoLiveData.observe(this) { bitmap ->
            binding.imageUser.setImageBitmap(bitmap)
        }
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                registrationViewModel.user.name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.editTextYourName.addTextChangedListener(titleWatcher)

        binding.imageUser.apply {
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            setOnClickListener {
                captureImageLauncher.launch(pickIntent)
            }
        }

        binding.buttonContinue.setOnClickListener{
            registrationViewModel.saveAccount()
        }
    }
}