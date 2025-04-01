package com.example.sharedcard.ui.group.join_group

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentJoinGroupBinding
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.example.sharedcard.util.isVisible
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class JoinGroupFragment : DialogFragment() {

    private val scanQrResultLauncher =
        registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
                viewModel.join(result.contents)
            }
        }
    private val viewModel by viewModels<GroupViewModel>({ activity as NavigationDrawerActivity }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentJoinGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinGroupBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if(it.state == Result.State.OK)
                dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        binding.run {
            joinButton.setOnClickListener {
                viewModel.join(
                    binding.dialogToHistoryEditView.text.toString()
                )
            }
            qrCodeButton.setOnClickListener {
                val scanOptions = ScanOptions()
                scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                scanOptions.setPrompt("Отсканируйте QR-code")
                scanOptions.setBeepEnabled(false)
                scanOptions.setBarcodeImageEnabled(false)
                scanQrResultLauncher.launch(scanOptions)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    companion object {
        const val DIALOG_JOIN_GROUP = "dialogJoinGroup"
    }
}