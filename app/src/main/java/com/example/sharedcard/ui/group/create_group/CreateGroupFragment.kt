package com.example.sharedcard.ui.group.create_group

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentCreateGroupBinding
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.util.appComponent

class CreateGroupFragment : DialogFragment() {


    private val viewModel: CreateGroupViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    private lateinit var binding: FragmentCreateGroupBinding

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stream = requireActivity().contentResolver.openInputStream(result.data?.data!!)
                val bitmap = BitmapFactory.decodeStream(stream)
                binding.dialogCreateGroupImage.setImageBitmap(bitmap)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_create_group,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            binding.dialogCreateGroupAddButton.isEnabled = it
        }
        viewModel.resultLiveData.observe(viewLifecycleOwner) {
            when (it.state) {
                Result.State.OK -> dismiss()
                Result.State.ERROR -> {
                    binding.viewLayout.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), it.error!!, Toast.LENGTH_SHORT).show()
                }

                Result.State.LOADING -> {
                    binding.viewLayout.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            dialogCreateGroupImage.setOnClickListener {
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"
                captureImageLauncher.launch(pickIntent)
            }
            dialogCreateGroupNameEditView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    viewModel.name = s.toString()
                }

            })
            dialogCreateGroupAddButton.setOnClickListener {
                viewModel.create(binding.dialogCreateGroupImage.drawToBitmap())
            }
        }
    }

    companion object {
        const val DIALOG_CREATE_GROUP = "dialogCreateGroup"
    }
}