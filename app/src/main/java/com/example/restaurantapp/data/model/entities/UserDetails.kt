package com.example.restaurantapp.data.model.entities

import com.squareup.moshi.Json

data class UserDetails(
    @Json(name = "firstName")
    var firstName: String? = "",

    @Json(name = "lastName")
    var lastName: String? = "",

    @Json(name = "phone")
    var phone: String? = ""
)
