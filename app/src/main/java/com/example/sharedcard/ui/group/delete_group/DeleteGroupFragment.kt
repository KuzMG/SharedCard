package com.example.sharedcard.ui.group.delete_group

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.util.appComponent
import java.util.UUID

class DeleteGroupFragment : DialogFragment() {

    private val viewModel by viewModels<GroupViewModel> {
        appComponent.multiViewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val idGroup = UUID.fromString(requireArguments().getString(KEY_ID))
        val nameGroup = requireArguments().getString(KEY_NAME)

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_group, nameGroup))
            .setPositiveButton("Да") { dialog, _ ->
                viewModel.deleteGroup(idGroup)
                dialog.cancel()
            }.create()
    }


    companion object {
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        const val DIALOG_DELETE = "dialogDelete"
        fun newInstance(id: UUID, name: String): DeleteGroupFragment {
            val dialog = DeleteGroupFragment()
            val args = Bundle()
            args.putString(KEY_ID, id.toString())
            args.putString(KEY_NAME, name)
            dialog.arguments = args
            return dialog
        }
    }
}

