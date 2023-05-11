package com.example.restaurantapp.data.model.entities

import com.squareup.moshi.Json

data class User(
    @Json(name = "id")
    val id: Long,

    @Json(name = "username")
    val username: String,

    @Json(name = "firstName")
    val firstName: String?,

    @Json(name = "lastName")
    val lastName: String?,

    @Json(name = "phone")
    val phone: String?
)
