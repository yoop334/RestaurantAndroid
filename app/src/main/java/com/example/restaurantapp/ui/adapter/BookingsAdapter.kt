package com.example.restaurantapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.data.model.entities.Booking
import com.example.restaurantapp.databinding.ItemBookingBinding
import com.example.restaurantapp.utils.CalendarManager
import com.example.restaurantapp.utils.DateFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingsAdapter(
    private val listener: BookingListener
) : RecyclerView.Adapter<BookingsAdapter.BookingsViewHolder>() {

    private var bookingsList = ArrayList<Booking>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        )

    override fun onBindViewHolder(holder: BookingsViewHolder, position: Int) {
        val booking = bookingsList[position]

        holder.binding.textViewPersonsInfo.text = booking.nrOfPersons.toString()
        holder.binding.textViewDateInfo.text = DateFormatter.getDateTimeFromMillis(booking.time)

        holder.binding.imageDelete.setOnClickListener {
            listener.onCancelClicked(booking.id, position)
        }
    }

    override fun getItemCount() = bookingsList.size


    fun setTasksList(transactions: List<Booking>) {
        bookingsList.clear()
        bookingsList.addAll(transactions)
        notifyItemRangeChanged(0, bookingsList.size)
    }

    fun notifyDataChanged(position: Int) {
        bookingsList.removeAt(position)
        notifyDataSetChanged()
    }


    class BookingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemBookingBinding.bind(itemView)
    }

    interface BookingListener {

        fun onCancelClicked(bookingId: Long, position: Int)
    }
}