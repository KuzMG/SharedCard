package com.example.sharedcard.ui.check.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.DialogFragmentCreateCheckBinding


class AddProductFragment : DialogFragment() {
    private lateinit var binding: DialogFragmentCreateCheckBinding
    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.page = requireArguments().getInt(KEY_PAGE)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_fragment_create_check,
                container,
                false
            )
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (viewModel.page) {
            0 -> {
                binding.dialogAddTextView.text = getString(R.string.dialog_add_product)
                binding.dialogNameEditText.hint = getString(R.string.dialog_text_hint_porduct)
                binding.dialogCountOfProductEditView.hint = getString(R.string.dialog_text_hint_count)
            }

            1 -> {
                binding.dialogAddTextView.text = getString(R.string.dialog_add_target)
                binding.dialogNameEditText.hint = getString(R.string.dialog_text_hint_target)
                binding.dialogCountOfProductEditView.hint = getString(R.string.dialog_text_hint_price)
            }
        }

    }

    companion object {
        const val KEY_PAGE = "page"
        fun newInstance(page: Int): AddProductFragment {
            val dialog = AddProductFragment()
            val args = Bundle()
            args.putInt(KEY_PAGE, page)
            dialog.arguments = args
            return dialog
        }
    }
}