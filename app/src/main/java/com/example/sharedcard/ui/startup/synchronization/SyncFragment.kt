package com.example.sharedcard.ui.startup.synchronization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.ui.startup.StartupViewModel.State.Continue
import com.example.sharedcard.ui.startup.StartupViewModel.State.Startup
import com.example.sharedcard.util.appComponent

class SyncFragment : Fragment() {

    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_sync, container, false)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.synchronizationState.observe(viewLifecycleOwner) {
            if(it.error != null){
                viewModel.setTransitionState(Startup)
            } else if(it.isSync) {
                viewModel.setTransitionState(Continue)
            }
        }
        viewModel.synchronization()
    }
}