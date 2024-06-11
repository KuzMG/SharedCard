package com.example.sharedcard.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import javax.inject.Inject

class RecipesFragment : Fragment() {


    private val viewModel by viewModels<RecipesViewModel> {
        appComponent.multiViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }


}