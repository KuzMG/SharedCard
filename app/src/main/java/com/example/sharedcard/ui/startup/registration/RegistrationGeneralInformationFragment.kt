package com.example.sharedcard.ui.startup.registration


import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentRegistrationGeneralInformationBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.util.appComponent
import java.text.SimpleDateFormat
import java.util.GregorianCalendar


class RegistrationGeneralInformationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationGeneralInformationBinding
    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationGeneralInformationBinding.inflate(
            inflater,
            container,
            false
        )

        binding.genderEditText.setAdapter(ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            arrayOf(getString(R.string.male), getString(R.string.female))
        ))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerFormState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            state.genderError?.let {
                binding.genderEditText.error = getString(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            birthdayButton.setOnClickListener {
                showDatePickerDialog { datePicker, i, i2, i3 ->
                    val calendar =
                        GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                    viewModel.regDate = calendar.time
                    val format = SimpleDateFormat(getString(R.string.date_format))
                    birthdayButton.text = format.format(viewModel.regDate)
                }
            }

            genderEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.regGender = p0.toString() == getString(R.string.male)
                }

                override fun afterTextChanged(p0: Editable?) {}
            })


            val genedr = getString(if (viewModel.regGender) R.string.male else R.string.female)
            genderEditText.setText(genedr,false)
            val format = SimpleDateFormat(getString(R.string.date_format))
            birthdayButton.text = format.format(viewModel.regDate)
        }
    }


    private fun showDatePickerDialog(click: DatePickerDialog.OnDateSetListener) {
        val calendar = GregorianCalendar()
        val year = calendar.get(GregorianCalendar.YEAR)
        val month = calendar.get(GregorianCalendar.MONTH)
        val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
        DatePickerDialog(
            requireContext(), R.style.DialogTheme,
            click,
            year,
            month,
            day
        ).show()
    }
}