package com.example.sharedcard.ui.group.edit_group

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.databinding.FragmentEditGroupBinding
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.util.appComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
                        viewModel.photo = bitmap
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
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_edit_group,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGroup().observe(viewLifecycleOwner) { group ->
            binding.dialogEditGroupNameEditView.setText(group.name)
        }
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            binding.dialogEditGroupAddButton.isEnabled = it
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
                viewModel.save()
            }
            dialogEditGroupNameEditView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    viewModel.name = s.toString()
                }

            })
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