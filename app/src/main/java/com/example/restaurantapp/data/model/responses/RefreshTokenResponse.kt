package com.example.restaurantapp.data.model.responses

data class RefreshTokenResponse (
    val accessToken: String,

    val refreshToken: String,

    val role: Int
)