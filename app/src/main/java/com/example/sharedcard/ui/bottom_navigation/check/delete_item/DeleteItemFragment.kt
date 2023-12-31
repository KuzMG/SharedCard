package com.example.sharedcard.ui.bottom_navigation.check.delete_item

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import java.util.UUID

class DeleteItemFragment : DialogFragment() {

    private val viewModel: DeleteItemViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        viewModel.page = requireArguments().getInt(KEY_PAGE)
        val idItem = UUID.fromString(requireArguments().getString(KEY_ID))
        val nameItem = requireArguments().getString(KEY_NAME) ?: ""

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_item, nameItem))
            .setPositiveButton("Да") { dialog, id ->
                viewModel.deleteItem(idItem)
                dialog.cancel()
            }.create()
    }


    companion object {
        private const val KEY_PAGE = "page"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        const val DIALOG_DELETE = "dialogDelete"
        fun newInstance(page: Int, id: UUID, name: String): DeleteItemFragment {
            val dialog = DeleteItemFragment()
            val args = Bundle()
            args.putInt(KEY_PAGE, page)
            args.putString(KEY_ID, id.toString())
            args.putString(KEY_NAME, name)
            dialog.arguments = args
            return dialog
        }
    }
}

