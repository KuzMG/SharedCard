package com.example.sharedcard.ui.bottom_navigation.check.delete_item

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R

class DeleteItemFragment : DialogFragment() {

    private val viewModel: DeleteItemViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        viewModel.page = requireArguments().getInt(KEY_PAGE)
        val idItem = requireArguments().getLong(KEY_ID)
        val nameItem = requireArguments().getString(KEY_NAME) ?: ""

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete, nameItem))
            .setPositiveButton("Да") { dialog, id ->
                viewModel.deleteItem(idItem)
                dialog.cancel()
            }.create()
    }


    companion object {
        const val KEY_PAGE = "page"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val DIALOG_DELETE = "dialogDelete"
        fun newInstance(page: Int, id: Long, name: String): DeleteItemFragment {
            val dialog = DeleteItemFragment()
            val args = Bundle()
            args.putInt(KEY_PAGE, page)
            args.putLong(KEY_ID, id)
            args.putString(KEY_NAME, name)
            dialog.arguments = args
            return dialog
        }
    }
}

