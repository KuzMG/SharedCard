package com.example.sharedcard.ui.group.create_group

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.databinding.FragmentCreateGroupBinding
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.util.appComponent

class CreateGroupFragment : Fragment() {


    private val viewModel: CreateGroupViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    private lateinit var binding: FragmentCreateGroupBinding

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stream = requireActivity().contentResolver.openInputStream(result.data?.data!!)
                val bitmap = BitmapFactory.decodeStream(stream)
                binding.groupImageView.setImageBitmap(bitmap)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGroupBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resultLiveData.observe(viewLifecycleOwner) {
            when (it.state) {
                Result.State.OK -> parentFragmentManager.popBackStack()
                Result.State.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (it.error is NoSuchFieldException) {
                        binding.dialogCreateGroupNameEditView.error = it.error.message
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.error?.message ?: "",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                Result.State.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        binding.run {
            groupCardView.setOnClickListener {
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"
                captureImageLauncher.launch(pickIntent)
            }
            addButton.setOnClickListener {
                viewModel.create(
                    binding.dialogCreateGroupNameEditView.text.toString(),
                    binding.groupImageView.drawToBitmap()
                )
            }
            appBar.toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }
}