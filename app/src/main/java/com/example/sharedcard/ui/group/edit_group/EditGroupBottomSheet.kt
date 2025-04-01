package com.example.sharedcard.ui.group.edit_group

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.databinding.FragmentEditGroupBinding
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import java.util.UUID
import javax.inject.Inject


class EditGroupBottomSheet : BottomSheetDialogFragment() {
    @Inject
    lateinit var factory: EditGroupViewModel.FactoryHelper
    private val viewModel: EditGroupViewModel by viewModels {
        factory.create(requireArguments().getString(KEY_ID, AppDatabase.DEFAULT_UUID))
    }
    private lateinit var binding: FragmentEditGroupBinding

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                requireActivity().contentResolver.openInputStream(result.data?.data!!)
                    .use { stream ->
                        val bitmap = BitmapFactory.decodeStream(stream)
                        binding.dialogEditGroupImage.setImageBitmap(bitmap)
                        viewModel.isImageChange = true
                    }

            }
        }

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditGroupBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGroup().observe(viewLifecycleOwner) { group ->
            binding.dialogEditGroupNameEditView.setText(group.name)
            Picasso.get().load(group.url).into(binding.dialogEditGroupImage)
        }
        viewModel.resultLiveData.observe(viewLifecycleOwner) {
            when (it.state) {
                Result.State.OK -> dismiss()
                Result.State.ERROR -> {
                    if (it.error is NoSuchFieldException) {
                        binding.dialogEditGroupNameEditView.error = it.error.message
                    } else {
                        binding.viewLayout.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.error!!.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Result.State.LOADING -> {
                    binding.viewLayout.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            dialogEditGroupImage.setOnClickListener {
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"
                captureImageLauncher.launch(pickIntent)
            }
            dialogEditGroupAddButton.setOnClickListener {
                viewModel.save(
                    dialogEditGroupNameEditView.text.toString(),
                    binding.dialogEditGroupImage.drawToBitmap()
                )
            }
        }
    }

    companion object {
        const val DIALOG_EDIT_GROUP = "dialogEditGroup"
        private const val KEY_ID = "id"
        fun newInstance(id: UUID): EditGroupBottomSheet {
            val dialog = EditGroupBottomSheet()
            val args = Bundle()
            args.putString(KEY_ID, id.toString())
            dialog.arguments = args
            return dialog
        }
    }
}