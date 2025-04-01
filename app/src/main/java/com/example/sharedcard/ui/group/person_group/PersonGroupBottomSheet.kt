package com.example.sharedcard.ui.group.person_group

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.sharedcard.databinding.FragmentUserOptionsBinding
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.example.sharedcard.util.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
import java.util.UUID

class PersonGroupBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentUserOptionsBinding

    private val viewModel by viewModels<PersonGroupViewModel> {
        appComponent.multiViewModelFactory
    }
    private lateinit var groupId: UUID
    private lateinit var personId: UUID
    private var statusMy: Int = GroupPersonsEntity.ADMIN
    private var statusUser: Int = GroupPersonsEntity.USER

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        personId = UUID.fromString(requireArguments().getString(KEY_ID_USER))
        groupId = UUID.fromString(requireArguments().getString(KEY_ID_GROUP))
        statusMy = requireArguments().getInt(KEY_STATUS_MY,GroupPersonsEntity.ADMIN)
        statusUser = requireArguments().getInt(KEY_STATUS_USER,GroupPersonsEntity.USER)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserOptionsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.run {
            makeAdminLayout.isVisible(statusMy == GroupPersonsEntity.CREATOR && statusUser == GroupPersonsEntity.USER)
            delAdminLayout.isVisible(statusMy == GroupPersonsEntity.CREATOR && statusUser == GroupPersonsEntity.ADMIN)
            deleteLayout.isVisible(statusMy != GroupPersonsEntity.USER)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendLiveData.observe(viewLifecycleOwner){
            it?.let {
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            makeAdminLayout.setOnClickListener {
                viewModel.setPersonStatus(personId, groupId,GroupPersonsEntity.ADMIN)
            }
            delAdminLayout.setOnClickListener {
                viewModel.setPersonStatus(personId, groupId,GroupPersonsEntity.USER)
            }
            deleteLayout.setOnClickListener {
                viewModel.deletePerson(personId, groupId)
            }
        }
    }

    companion object {
        const val DIALOG_JOIN_GROUP = "dialogUserGroup"
        private const val KEY_ID_GROUP = "idGroup"
        private const val KEY_ID_USER = "idUser"
        private const val KEY_STATUS_MY = "statusMy"
        private const val KEY_STATUS_USER = "statusUser"
        fun newInstance(idUser: UUID, idGroup: UUID,statusMy: Int,statusUser: Int): PersonGroupBottomSheet {
            val dialog = PersonGroupBottomSheet()
            val args = Bundle()
            args.putString(KEY_ID_USER, idUser.toString())
            args.putString(KEY_ID_GROUP, idGroup.toString())
            args.putInt(KEY_STATUS_MY, statusMy)
            args.putInt(KEY_STATUS_USER, statusUser)
            dialog.arguments = args
            return dialog
        }
    }
}