package com.example.restaurantapp.data.model.entities

import com.squareup.moshi.Json

data class BookingUser(
    @Json(name = "firstName")
    var firstName: String? = "",

    @Json(name = "lastName")
    var lastName: String? = "",

    @Json(name = "phone")
    var phone: String? = "",

    @Json(name = "nrOfPersons")
    var nrOfPersons: Int = 0,

    @Json(name = "time")
    var time: Long = 0,
)