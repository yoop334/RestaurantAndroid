package com.example.restaurantapp.data.model.requests

data class RefreshToken (
    val accessToken: String,

    val refreshToken: String
)