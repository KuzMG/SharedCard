package com.example.sharedcard.ui.group.delete_group

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.R
import com.example.sharedcard.ui.group.GroupFragment
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerViewModel
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import java.util.UUID

class DeleteGroupFragment : DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val idGroup = UUID.fromString(requireArguments().getString(KEY_ID))
        val nameGroup = requireArguments().getString(KEY_NAME)
        val viewModel = ViewModelProvider(requireActivity() as NavigationDrawerActivity,appComponent.multiViewModelFactory)[GroupViewModel::class.java]
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_group, nameGroup))
            .setPositiveButton("Да") { dialog, _ ->
                viewModel.deleteGroup(isInternetConnection(requireContext()), idGroup)
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

