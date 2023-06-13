package com.example.restaurantapp.di

import android.app.Application
import com.example.restaurantapp.data.remote.RestaurantApi
import com.example.restaurantapp.utils.interceptor.AuthInterceptor
import com.example.restaurantapp.utils.preferences.PreferenceHelper
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(moshiConverterFactory: MoshiConverterFactory, okHttpClient: OkHttpClient):
            Retrofit {

        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideTaskyApi(retrofit: Retrofit): RestaurantApi {
        return retrofit.create(RestaurantApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder().build()

        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(app: Application, preferenceHelper: PreferenceHelper): OkHttpClient {
        val cacheDir = File(app.cacheDir, "http")
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(preferenceHelper))
            .cache(Cache(cacheDir, DISK_CACHE_SIZE))
            .build()
    }

    companion object {
        private const val DISK_CACHE_SIZE = (50 * 1024 * 1024).toLong()
        const val API_URL = "http://10.0.2.2:5007"
        const val LOGIN = "/api/account/login"
        const val LOGOUT = "/api/account/logout"
        const val REFRESH = "/api/account/refresh-token"
        const val USER_DETAILS = "/api/users"
        const val PERSONS_FOR_DATE = "/api/bookings/{date}"
        const val HOURS_FOR_DATE = "/api/bookings/{date}/{nrOfPersons}"
        const val ADD_BOOKING = "/api/bookings"
        const val GET_ALL_BOOKINGS = "/api/bookings"
        const val GET_FIRST_BOOKINGS = "/api/bookings/first"
        const val GET_ALL_BOOKINGS_BY_DATE = "/api/bookings/all/{date}"
        const val CREATE_USER = "/api/users"
        const val UPDATE_USER = "/api/users"
        const val CANCEL_BOOKING = "/api/bookings/{id}"
    }
}