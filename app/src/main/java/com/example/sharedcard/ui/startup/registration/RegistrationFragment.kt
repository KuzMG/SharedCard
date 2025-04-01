package com.example.sharedcard.ui.startup.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentRegistrationBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.ui.startup.StartupViewModel.State.Synchronization
import com.example.sharedcard.util.appComponent
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

class RegistrationFragment : Fragment() {


    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentRegistrationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.viewPager.adapter =  ViewPagerAdapter(childFragmentManager,lifecycle)
        binding.viewPager.offscreenPageLimit = 5
        binding.viewPager.isUserInputEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentPageLivedata.observe(viewLifecycleOwner) {
            binding.viewPager.setCurrentItem(it-1,false)
            binding.continueButton.text = when(it){
                5  -> getString(R.string.registration_create_button)
                else -> getString(R.string.registration_continue_button)
            }
        }
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            loading(result.loading)
            if (!result.loading) {
                result.message?.let {
                    showLoginFailed(result.message, result.error)
                }
                if (result.codeSend) {
                    viewModel.currentPageReg++
                }
                if(result.isContinue){
                    viewModel.setTransitionState(Synchronization)
                }
            }
        }

        viewModel.registerFormState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            if (state.page ==binding.viewPager.currentItem) {
                binding.continueButton.isEnabled = state.isDataValid
            } else if(binding.viewPager.currentItem == 2){
                binding.continueButton.isEnabled = true
            }
        }
    }


    private fun loading(flag: Boolean) {
        binding.continueButton.isEnabled = !flag
        binding.progressBar.visibility = isVisible(flag)
        binding.viewPager.visibility = isVisible(!flag)
    }


    private fun isVisible(flag: Boolean) =
        if (flag) View.VISIBLE else View.GONE

    val callback = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val page = binding.viewPager
            val child = page.children.first() as RecyclerView
            if(child.childCount == 0)
                return
            when(position){
                0 -> {
                    val nameEditText = page.findViewById<TextInputEditText>(R.id.name_edit_text)
                    nameEditText.setText(viewModel.regName)
                }
                1 -> {
                    val emailEditText = page.findViewById<TextInputEditText>(R.id.email_edit_text)
                    emailEditText.setText(viewModel.regEmail)
                }
                2 -> {
                    val weightButton = page.findViewById<Button>(R.id.weight_button)
                    val heightButton = page.findViewById<Button>(R.id.height_button)
                    val birthDayButton = page.findViewById<Button>(R.id.birthday_button)

                    weightButton.text = getString(R.string.weight,viewModel.regWeight.toString())
                    heightButton.text = getString(R.string.height,viewModel.regHeight.toString())


                    val format = SimpleDateFormat(getString(R.string.date_format))
                    birthDayButton.text = format.format(viewModel.regDate)


                }
                3 -> {
                    val passwordEditText = page.findViewById<TextInputEditText>(R.id.password_edit_text)
                    passwordEditText.setText(viewModel.regPassword)
                }
                4 -> {
                    val codeEditText = page.findViewById<TextInputEditText>(R.id.code_edit_text)
                    codeEditText.setText(viewModel.regCode)
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        binding.viewPager.registerOnPageChangeCallback(callback)
        binding.continueButton.setOnClickListener {
            when (viewModel.currentPageReg) {
                4 -> viewModel.registration()
                5 -> viewModel.verification()
                else -> {
                    viewModel.currentPageReg++
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.viewPager.unregisterOnPageChangeCallback(callback)
    }
    private fun showLoginFailed(@StringRes errorString: Int, e: Exception?) {
        val appContext = context?.applicationContext ?: return
        val message = getString(errorString)
        Toast.makeText(appContext, message + e?.message, Toast.LENGTH_LONG).show()
    }

    class ViewPagerAdapter(fragment: FragmentManager,lifecycle : Lifecycle) : FragmentStateAdapter(fragment,lifecycle) {
        override fun getItemCount() = 5

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> RegistrationNameFragment()
            1 -> RegistrationEmailFragment()
            2 -> RegistrationGeneralInformationFragment()
            3 -> RegistrationPasswordFragment()
            4 -> RegistrationCodeFragment()
            else -> throw IndexOutOfBoundsException()
        }


    }
}