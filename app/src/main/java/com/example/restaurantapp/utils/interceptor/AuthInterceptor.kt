package com.example.restaurantapp.utils.interceptor

import com.example.restaurantapp.utils.preferences.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferenceHelper: PreferenceHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var newRequest = chain.request()

        val token = preferenceHelper.getToken()
        if (token.isNotEmpty()) {
            newRequest = newRequest.newBuilder()
                .addHeader(
                    "Authorization", "Bearer $token"
                )
                .build()
        }

        return chain.proceed(newRequest)
    }
}
