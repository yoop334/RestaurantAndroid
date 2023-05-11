package com.example.restaurantapp.data.repositories

import com.example.restaurantapp.data.model.entities.Booking
import com.example.restaurantapp.data.remote.RestaurantApi
import com.example.restaurantapp.utils.preferences.PreferenceHelper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BookingsRepository @Inject constructor(
    private val restaurantApi: RestaurantApi,
    private val preferenceHelper: PreferenceHelper
) {

    fun addBooking(booking: Booking): Single<Boolean> {
        return Single.create { emitter ->

            val result = restaurantApi.addBooking(booking).execute()

            if (result.isSuccessful) {
                if (result.body() != null || result.body() != false) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onError(Exception("Error"))
                }
            } else {
                emitter.onError(Exception("Error"))
            }
        }
    }

    fun getAllBookings(): Single<List<Booking>> {
        return Single.create { emitter ->

            val result = restaurantApi.getAllBookings().execute()

            if (result.isSuccessful) {
                result.body()?.let { bookings ->
                    emitter.onSuccess(bookings)
                }
            } else {
                emitter.onError(Exception("Error"))
            }
        }
    }

    fun getAvailableNrPersonsForDate(date: Long): Single<List<Int>> {
        return Single.create { emitter ->

            val result = restaurantApi.getAvailableNrPersonsForDate(date).execute()

            if (result.isSuccessful) {
                result.body()?.let { persons ->
                    emitter.onSuccess(persons)
                }
            } else {
                emitter.onError(Exception("Error"))
            }
        }
    }

    fun getAvailableHours(date: Long, nrPersons: Int): Single<List<Int>> {
        return Single.create { emitter ->

            val result = restaurantApi.getAvailableHours(date, nrPersons).execute()

            if (result.isSuccessful) {
                result.body()?.let { hours ->
                    emitter.onSuccess(hours)
                }
            } else {
                emitter.onError(Exception("Error"))
            }
        }
    }

    fun cancelBooking(bookingId: Long): Single<Boolean> {
        return Single.create { emitter ->

            val result = restaurantApi.cancelBooking(bookingId).execute()

            if (result.isSuccessful) {
                result.body()?.let { hours ->
                    emitter.onSuccess(hours)
                }
            } else {
                emitter.onError(Exception("Error"))
            }
        }
    }
}