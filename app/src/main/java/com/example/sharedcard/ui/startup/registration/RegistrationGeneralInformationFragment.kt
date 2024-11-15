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
import androidx.databinding.DataBindingUtil
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration_general_information,
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
            heightButton.setOnClickListener {
                showNumberPickerDialog(Array(200) {
                    (50 + it).toString()
                }, R.string.registration_height_label, viewModel.regHeight.toString()) { p0 ->
                    heightButton.text = getString(R.string.height, p0.toString())
                    viewModel.regHeight = p0.toInt()
                }
            }
            weightButton.setOnClickListener {
                showNumberPickerDialog(Array(320) {
                    (40F + (it.toFloat()) / 2).toString()
                }, R.string.registration_weight_label, viewModel.regWeight.toString()) { p0 ->
                    weightButton.text = getString(R.string.weight, p0)
                    viewModel.regWeight = p0.toDouble()
                }
            }
            genderEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.regGender = p0.toString() == getString(R.string.male)
                }

                override fun afterTextChanged(p0: Editable?) {}
            })


            weightButton.text = getString(R.string.weight, viewModel.regWeight.toString())
            heightButton.text = getString(R.string.height, viewModel.regHeight.toString())
            val genedr = getString(if (viewModel.regGender) R.string.male else R.string.female)
            genderEditText.setText(genedr,false)
            val format = SimpleDateFormat(getString(R.string.date_format))
            birthdayButton.text = format.format(viewModel.regDate)
        }
    }

    private fun showNumberPickerDialog(
        array: Array<String>,
        @StringRes
        title: Int,
        value: String,
        click: (String) -> Unit
    ) {
        val dialog = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.number_picker_dialog, null)
        dialog.setTitle(title)
        dialog.setView(view)
        val numberPicker = view.findViewById<NumberPicker>(R.id.dialog_number_picker)

        numberPicker.displayedValues = array
        numberPicker.wrapSelectorWheel = false
        numberPicker.minValue = 0
        numberPicker.maxValue = array.size
        numberPicker.value = array.lastIndexOf(value)
        dialog.setPositiveButton(getString(R.string.dialog_ok_button)) { dialogInterface, i ->
            click.invoke(array[numberPicker.value])
        }
        dialog.setNegativeButton(getString(R.string.dialog_cancel_button)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.create().show()
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