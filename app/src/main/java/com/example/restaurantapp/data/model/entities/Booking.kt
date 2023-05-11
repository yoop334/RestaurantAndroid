package com.example.restaurantapp.data.model.entities

import com.squareup.moshi.Json

data class Booking(
    @Json(name = "id")
    val id: Long = 0,

    @Json(name = "date")
    var date: Long = 0,

    @Json(name = "nrOfPersons")
    var nrOfPersons: Int = 0,

    @Json(name = "time")
    var time: Long = 0,
)
