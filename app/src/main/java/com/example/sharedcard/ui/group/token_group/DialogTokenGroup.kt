package com.example.sharedcard.ui.group.token_group

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentTokenGroupBinding
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.util.appComponent
import java.util.UUID


class DialogTokenGroup : DialogFragment() {
    private lateinit var binding: FragmentTokenGroupBinding
    lateinit var idGroup: UUID

    val viewModel: GroupViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_token_group, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getToken(idGroup).observe(viewLifecycleOwner) { token ->
            binding.copyView.isClickable = true
            binding.progressBar.visibility = View.INVISIBLE
            binding.groupFragmentTokenTextView.text = token
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        idGroup = UUID.fromString(requireArguments().getString(KEY_ID))
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onStart() {
        super.onStart()
        binding.copyView.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("dd", binding.groupFragmentTokenTextView.text.toString())
            clipboard.setPrimaryClip(clip)
        }
    }

    companion object {
        const val DIALOG_TOKEN_GROUP = "dialogTokenGroup"
        private const val KEY_ID = "id"
        fun newInstance(id: UUID): DialogTokenGroup {
            val dialog = DialogTokenGroup()
            val args = Bundle()
            args.putString(KEY_ID, id.toString())
            dialog.arguments = args
            return dialog
        }
    }

}