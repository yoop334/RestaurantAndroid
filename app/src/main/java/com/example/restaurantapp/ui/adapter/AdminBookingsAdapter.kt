package com.example.restaurantapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.data.model.entities.BookingUser
import com.example.restaurantapp.databinding.ItemBookingUserBinding
import com.example.restaurantapp.utils.DateFormatter

class AdminBookingsAdapter : RecyclerView.Adapter<AdminBookingsAdapter.AdminBookingsViewHolder>() {

    private var bookingsList = ArrayList<BookingUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdminBookingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_booking_user, parent, false)
        )

    override fun getItemCount() = bookingsList.size

    override fun onBindViewHolder(holder: AdminBookingsViewHolder, position: Int) {
        val booking = bookingsList[position]

        holder.binding.textViewPersonsInfo.text = booking.nrOfPersons.toString()
        holder.binding.textViewDateInfo.text = DateFormatter.getDateTimeFromMillis(booking.time)
        holder.binding.textViewNameInfo.text = booking.firstName + " " + booking.lastName
        holder.binding.textViewPhoneInfo.text = booking.phone
    }

    fun setBookingsList(bookings: List<BookingUser>) {
        bookingsList.clear()
        bookingsList.addAll(bookings)
        notifyDataSetChanged()
    }

    class AdminBookingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemBookingUserBinding.bind(itemView)
    }
}