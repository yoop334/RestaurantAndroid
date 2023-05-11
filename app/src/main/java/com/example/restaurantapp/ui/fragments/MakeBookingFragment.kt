package com.example.restaurantapp.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentMakeBookingBinding
import com.example.restaurantapp.ui.activities.BaseActivity
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.viewmodels.BookingsViewModel
import com.example.restaurantapp.utils.CalendarManager
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class MakeBookingFragment : ValidatorFragment<FragmentMakeBookingBinding>(), BookingsViewModel.MakeBookingListener {

    private val viewModel by activityViewModels<BookingsViewModel>()

    private val disposable = CompositeDisposable()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentMakeBookingBinding.inflate(inflater, container, false)

        if (rootView == null) {
            rootView = viewBinding!!.root
            isFirstLoaded = true
            onBackPressListener()
        } else {
            isFirstLoaded = false
        }
    }

    override fun getTitle(): String {
        return "Make a transaction"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstLoaded) {
            initDatePicker()
            initButtons()
            viewModel.setListener(this)
            addValidationListeners()
            viewModel.clearData()
        }
    }

    private fun initButtons() {
        binding.buttonMakeReservation.setOnClickListener {
            if (!viewModel.isTransactionValid()) {
                validate(viewModel.values.ALL_TRANSACTION_FIELDS, viewModel.getTransactionCompletedFields())
            } else {
                addTransaction()
            }
        }
    }

    private fun addTransaction() {
        disposable.add(
            viewModel.addBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result == true) {
                        (activity as BaseActivity).getFragmentNavigation()
                            .replaceFragment(BookingsFragment())
                    }
                }, { throwable ->
                    throwable.message?.let { safeThrowable ->
                        Log.e(
                            MakeBookingFragment::class.java.canonicalName,
                            safeThrowable
                        )
                    }
                })
        )
    }

    private fun initDatePicker() {
        val editText = binding.textInputDate.editText ?: return

        editText.inputType = InputType.TYPE_NULL
        editText.keyListener = null
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.callOnClick()
            }
        }
        editText.setOnClickListener {
            CalendarManager().openDatePickerDialog(editText, requireContext())
        }
    }

    private fun initNrOfPersonsDropdown(date: Long) {
        disposable.add(
            viewModel.getAvailableNrPersonsForDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    (binding.textInputPersons.editText as? AutoCompleteTextView)?.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            R.layout.item_persons_list,
                            result
                        )
                    )
                }, { throwable ->
                    throwable.message?.let { safeThrowable ->
                        Log.e(
                            MakeBookingFragment::class.java.canonicalName,
                            safeThrowable
                        )
                    }
                })
        )
    }

    private fun initHoursDropdown(date: Long, nrPersons: Int) {
        disposable.add(
            viewModel.getAvailableHours(date, nrPersons)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    (binding.textInputHour.editText as? AutoCompleteTextView)?.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            R.layout.item_persons_list,
                            result
                        )
                    )
                }, { throwable ->
                    throwable.message?.let { safeThrowable ->
                        Log.e(
                            MakeBookingFragment::class.java.canonicalName,
                            safeThrowable
                        )
                    }
                })
        )
    }

    private fun onBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as MainActivity).onBackPressMain()
        }
    }

    override fun getTextInputsMap(): Map<String, TextInputLayout> {
        val map = HashMap<String, TextInputLayout>()

        map[viewModel.values.DATE_VALUE] = binding.textInputDate
        map[viewModel.values.PERSONS_VALUE] = binding.textInputPersons
        map[viewModel.values.TIME_VALUE] = binding.textInputHour

        return map
    }

    private fun addValidationListeners() {
        addValidationListener(binding.textInputDate, viewModel.values.DATE_VALUE)
        { x: String, y: String -> viewModel.setTransactionField(x, y) }
        addValidationListener(binding.textInputPersons, viewModel.values.PERSONS_VALUE)
        { x: String, y: String -> viewModel.setTransactionField(x, y) }
        addValidationListener(binding.textInputHour, viewModel.values.TIME_VALUE)
        { x: String, y: String -> viewModel.setTransactionField(x, y) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    override fun hasTopBar(): Boolean = true

    override fun dateSelected(date: Long) {
        initNrOfPersonsDropdown(date)
    }

    override fun personsSelected(date: Long, persons: Int) {
        initHoursDropdown(date, persons)
    }
}