package com.example.restaurantapp.data.model.responses


import com.example.restaurantapp.data.model.entities.User
import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "accessToken")
    val accessToken: String,

    @Json(name = "refreshToken")
    val refreshToken: String,

    @Json(name = "user")
    val user: User
)