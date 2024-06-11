package com.example.sharedcard.ui.check.delete_item

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.util.appComponent
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

class DeleteItemFragment : DialogFragment() {
    @Inject
    lateinit var factory: DeleteItemViewModel.FactoryHelper
    private val viewModel: DeleteItemViewModel by viewModels {
        factory.create(requireArguments().getInt(KEY_PAGE))
    }

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val idItem = UUID.fromString(requireArguments().getString(KEY_ID))
        val nameItem = requireArguments().getString(KEY_NAME) ?: ""

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_item, nameItem))
            .setPositiveButton("Да") { dialog, _ ->
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

