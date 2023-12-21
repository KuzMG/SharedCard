package com.example.sharedcard.ui.bottom_navigation.group.edit_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentEditGroupBinding
import com.example.sharedcard.ui.bottom_navigation.check.delete_item.DeleteItemFragment
import java.util.UUID

class EditGroupFragment : DialogFragment() {


    private val viewModel: EditGroupViewModel by viewModels()
    private lateinit var binding: FragmentEditGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.idGroup =UUID.fromString(arguments?.getString(KEY_ID)!!)
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
        viewModel.getGroup().observe(viewLifecycleOwner){ group ->
            binding.dialogEditGroupNameEditView.setText(group.name)
        }
    }

    companion object{
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