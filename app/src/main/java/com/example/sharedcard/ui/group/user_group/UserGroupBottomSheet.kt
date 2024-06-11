package com.example.sharedcard.ui.group.user_group

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentUserOptionsBinding
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.util.appComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.UUID

class UserGroupBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentUserOptionsBinding

    private val viewModel by viewModels<GroupViewModel> {
        appComponent.multiViewModelFactory
    }
    private lateinit var idGroup: UUID
    private lateinit var idUser: UUID


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        idUser = UUID.fromString(requireArguments().getString(KEY_ID_USER))
        idGroup = UUID.fromString(requireArguments().getString(KEY_ID_GROUP))
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_user_options,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            adminLayout.setOnClickListener {
                viewModel.makeUserAdmin(idUser, idGroup)
            }
            deleteLayout.setOnClickListener {
                viewModel.deleteUser(idUser, idGroup)
            }
        }
    }

    companion object {
        const val DIALOG_JOIN_GROUP = "dialogUserGroup"
        private const val KEY_ID_GROUP = "idGroup"
        private const val KEY_ID_USER = "idUser"
        fun newInstance(idUser: UUID, idGroup: UUID): UserGroupBottomSheet {
            val dialog = UserGroupBottomSheet()
            val args = Bundle()
            args.putString(KEY_ID_USER, idUser.toString())
            args.putString(KEY_ID_GROUP, idGroup.toString())
            dialog.arguments = args
            return dialog
        }
    }
}