package com.example.sharedcard.ui.bottom_navigation.group.edit_group

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentEditGroupBinding
import java.io.ByteArrayOutputStream
import java.util.UUID


class EditGroupFragment : DialogFragment() {


    private val viewModel: EditGroupViewModel by viewModels()
    private lateinit var binding: FragmentEditGroupBinding

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val stream = requireActivity().contentResolver.openInputStream(result.data?.data!!)
                val bitmap = BitmapFactory.decodeStream(stream)
                stream?.close()
                binding.dialogEditGroupImage.setImageBitmap(bitmap)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
                viewModel.photo = outputStream.toByteArray()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = requireArguments().getString(KEY_ID)
        viewModel.idGroup = UUID.fromString(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        }
    }

    companion object {
        const val DIALOG_EDIT_GROUP = "dialogEditGroup"
        private const val KEY_ID = "id"
        fun newInstance(id: UUID): EditGroupFragment {
            val dialog = EditGroupFragment()
            val args = Bundle()
            args.putString(KEY_ID, id.toString())
            dialog.arguments = args
            return dialog
        }
    }
}