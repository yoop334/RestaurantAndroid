package com.example.restaurantapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.databinding.FragmentBookingsBinding
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.adapter.BookingsAdapter
import com.example.restaurantapp.ui.viewmodels.BookingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class BookingsFragment : BaseFragment<FragmentBookingsBinding>(), BookingsAdapter.BookingListener {

    private lateinit var bookingsAdapter: BookingsAdapter

    private val viewModel by activityViewModels<BookingsViewModel>()

    private val disposable = CompositeDisposable()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentBookingsBinding.inflate(inflater, container, false)

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
            bookingsAdapter = BookingsAdapter(this)
            initRecyclerView()
            loadData()
            scroll()
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.transactionsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = bookingsAdapter
    }

    private fun loadData() {
        disposable.add(
            viewModel.getAllBookings()
                .subscribe(
                    { transactions ->
                        bookingsAdapter.setTasksList(transactions)
                        val pos = viewModel.getCurrentPosition()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.transactionsRecyclerView.scrollToPosition(pos)
                        }, 200)

                    },
                    { throwable ->
                        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                    }
                )
        )
    }

    private fun cancelBooking(bookingId: Long, position: Int) {
        disposable.add(
            viewModel.cancelBooking(bookingId)
                .subscribe(
                    { result ->
                        if (result) {
                            bookingsAdapter.notifyDataChanged(position)
                        }
                    },
                    { throwable ->
                        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                    }
                )
        )
    }

    private fun scroll() {
        binding.transactionsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                viewModel.setCurrentPosition(lastPosition)
            }
        })

    }

    override fun getTitle(): String {
        return "Toate rezervarile"
    }

    private fun onBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as MainActivity).onBackPressMain()
        }
    }

    override fun hasTopBar(): Boolean = true

    override fun onCancelClicked(bookingId: Long, position: Int) {
        context?.let { safeContext ->
            AlertDialog.Builder(safeContext)
                .setTitle("Rezervare")
                .setMessage("Sunteti sigur ca vreti sa anulati rezervarea?")
                .setPositiveButton("Da") { _, _ ->
                    cancelBooking(bookingId, position)
                }
                .setNegativeButton("Nu") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}