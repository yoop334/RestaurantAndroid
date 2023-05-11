package com.example.restaurantapp.data.remote

import com.example.restaurantapp.data.model.entities.Account
import com.example.restaurantapp.data.model.entities.Booking
import com.example.restaurantapp.data.model.entities.User
import com.example.restaurantapp.data.model.entities.UserDetails
import com.example.restaurantapp.data.model.requests.RefreshToken
import com.example.restaurantapp.data.model.responses.LoginResponse
import com.example.restaurantapp.di.NetworkModule
import retrofit2.Call
import retrofit2.http.*

interface RestaurantApi {

    @POST(NetworkModule.LOGIN)
    fun login(@Body account: Account): Call<LoginResponse>

    @POST(NetworkModule.LOGOUT)
    fun logout(): Call<Boolean>

    @PUT(NetworkModule.REFRESH)
    fun refreshToken(@Body refreshToken: RefreshToken): Call<RefreshToken>

    @GET(NetworkModule.USER_DETAILS)
    fun getUserDetails(): Call<UserDetails>

    @GET(NetworkModule.PERSONS_FOR_DATE)
    fun getAvailableNrPersonsForDate(@Path("date") date: Long): Call<List<Int>>

    @GET(NetworkModule.HOURS_FOR_DATE)
    fun getAvailableHours(@Path("date") date: Long, @Path("nrOfPersons") persons: Int): Call<List<Int>>

    @POST(NetworkModule.ADD_BOOKING)
    fun addBooking(@Body booking: Booking): Call<Boolean>

    @GET(NetworkModule.GET_ALL_BOOKINGS)
    fun getAllBookings(): Call<List<Booking>>

    @POST(NetworkModule.CREATE_USER)
    fun createUser(@Body account: Account): Call<User>

    @PUT(NetworkModule.UPDATE_USER)
    fun updateUser(@Body userDetails: UserDetails): Call<Boolean>

    @DELETE(NetworkModule.CANCEL_BOOKING)
    fun cancelBooking(@Path("id") id: Long): Call<Boolean>
}