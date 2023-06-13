package com.example.restaurantapp.ui.fragments.admin

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.FragmentBookingsAdminBinding
import com.example.restaurantapp.ui.activities.AdminActivity
import com.example.restaurantapp.ui.adapter.AdminBookingsAdapter
import com.example.restaurantapp.ui.fragments.BaseFragment
import com.example.restaurantapp.ui.viewmodels.BookingsViewModel
import com.example.restaurantapp.utils.CalendarManager
import com.example.restaurantapp.utils.DateFormatter.getDateTimeFromMillis
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AdminBookingsFragment : BaseFragment<FragmentBookingsAdminBinding>() {

    private val viewModel by activityViewModels<BookingsViewModel>()

    private val disposable = CompositeDisposable()

    private lateinit var bookingsAdapter: AdminBookingsAdapter

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentBookingsAdminBinding.inflate(inflater, container, false)

        if (rootView == null) {
            rootView = viewBinding!!.root
            isFirstLoaded = true
            onBackPressListener()
        } else {
            isFirstLoaded = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstLoaded) {
            bookingsAdapter = AdminBookingsAdapter()
            initRecyclerView()
            initDatePicker()
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.bookingsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = bookingsAdapter
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
        editText.addTextChangedListener {
            loadData(editText.text.toString())
        }

        editText.setText(
            getDateTimeFromMillis(
                System.currentTimeMillis(),
                SimpleDateFormat(
                    CalendarManager.DATE_FORMAT,
                    Locale.ENGLISH
                )
            )
        )
        loadData(editText.text.toString())
    }


    private fun loadData(value: String) {
        val date: Date? = SimpleDateFormat(CalendarManager.DATE_FORMAT, Locale.ENGLISH).parse(value)
        date?.let { safeDate ->
            disposable.add(
                viewModel.getAllBookingsByDate(safeDate.time)
                    .subscribe(
                        { bookings ->
                            bookingsAdapter.setBookingsList(bookings)
                        },
                        { throwable ->
                            Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                        }
                    )
            )
        }
    }

    override fun getTitle(): String {
        return "Rezervari"
    }

    override fun hasTopBar(): Boolean = true

    private fun onBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as AdminActivity).onBackPressMain()
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}