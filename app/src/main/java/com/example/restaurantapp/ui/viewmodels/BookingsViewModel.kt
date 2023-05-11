package com.example.restaurantapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.restaurantapp.data.model.entities.Booking
import com.example.restaurantapp.data.repositories.BookingsRepository
import com.example.restaurantapp.data.repositories.UserRepository
import com.example.restaurantapp.utils.CalendarManager.Companion.DATE_FORMAT
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val bookingsRepository: BookingsRepository,
) : ViewModel() {

    private var makeBookingListener: MakeBookingListener? = null

    val values = Companion

    private val completedFieldsForTransaction: MutableSet<String> = mutableSetOf()

    private val booking = Booking()

    private var currentPosition = 0

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    fun setCurrentPosition(pos: Int) {
        currentPosition = pos
    }

    fun setTransactionField(value: String, type: String) {
        when (type) {
            DATE_VALUE -> {
                val date: Date? =
                    SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(value)
                if (date != null) {
                    booking.date = date.time
                    makeBookingListener?.dateSelected(booking.date)
                }
            }
            PERSONS_VALUE -> {
                booking.nrOfPersons = value.toInt()
                if (booking.nrOfPersons != 0 && booking.date != 0L) {
                    makeBookingListener?.personsSelected(booking.date, booking.nrOfPersons)
                }
            }
            TIME_VALUE -> {
                booking.time = value.toLong()
            }
        }

        if (value.isEmpty()) {
            completedFieldsForTransaction.remove(type)
        } else {
            completedFieldsForTransaction.add(type)
        }
    }

    fun isTransactionValid(): Boolean {
        return completedFieldsForTransaction.size == NUMBER_OF_FIELDS
    }

    fun getTransactionCompletedFields(): MutableSet<String> {
        return completedFieldsForTransaction
    }

    fun getAvailableNrPersonsForDate(date: Long): Single<List<Int>> {
        return Single.create { emitter ->
            bookingsRepository.getAvailableNrPersonsForDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ result ->
                    emitter.onSuccess(result)
                }, { throwable ->
                    emitter.onError(throwable)
                })
        }
    }

    fun getAvailableHours(date: Long, nrPersons: Int): Single<List<Int>> {
        return Single.create { emitter ->
            bookingsRepository.getAvailableHours(date, nrPersons)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ result ->
                    emitter.onSuccess(result)
                }, { throwable ->
                    emitter.onError(throwable)
                })
        }
    }

    fun setListener(listener: MakeBookingListener) {
        makeBookingListener = listener
    }

    fun addBooking(): Single<Boolean> {
        return Single.create { emitter ->
            bookingsRepository.addBooking(booking)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result) {
                        emitter.onSuccess(true)
                    } else {
                        emitter.onError(Exception("Error"))
                    }
                }, { throwable ->
                    emitter.onError(throwable)
                })
        }
    }

    fun getAllBookings(): Single<List<Booking>> {
        return bookingsRepository.getAllBookings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun cancelBooking(bookingId: Long): Single<Boolean> {
        return bookingsRepository.cancelBooking(bookingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun clearData() {
        booking.date = 0
        booking.nrOfPersons = 0
        booking.time = 0
        completedFieldsForTransaction.clear()
    }

    companion object {
        const val DATE_VALUE = "date"
        const val PERSONS_VALUE = "persons"
        const val TIME_VALUE = "time"
        val ALL_TRANSACTION_FIELDS = listOf(DATE_VALUE, PERSONS_VALUE, TIME_VALUE)
        const val NUMBER_OF_FIELDS = 3
    }

    interface MakeBookingListener {

        fun dateSelected(date: Long)

        fun personsSelected(date: Long, persons: Int)
    }
}