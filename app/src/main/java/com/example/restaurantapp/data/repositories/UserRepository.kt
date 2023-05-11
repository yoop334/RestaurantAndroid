package com.example.restaurantapp.data.repositories

import com.example.restaurantapp.data.model.entities.UserDetails
import com.example.restaurantapp.data.remote.RestaurantApi
import com.example.restaurantapp.utils.preferences.PreferenceHelper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val restaurantApi: RestaurantApi,
    private val preferenceHelper: PreferenceHelper
) {

    fun updateUser(userDetails: UserDetails): Single<Boolean> {
        return Single.create { emitter ->
            val userId = preferenceHelper.getUserId()
            val response = restaurantApi.updateUser(userDetails).execute()

            if (!response.isSuccessful) {
                emitter.onError(Exception())
            } else {

                val data = response.body()
                if (data != null) {
                    emitter.onSuccess(true)
                }
            }
        }
    }

    fun getUserDetails(): Single<UserDetails> {
        return Single.create { emitter ->
            val response = restaurantApi.getUserDetails().execute()

            if (!response.isSuccessful) {
                emitter.onError(Exception())
            } else {

                val data = response.body()
                if (data != null) {
                    emitter.onSuccess(data)
                }
            }
        }
    }
//
//    fun getMoney(): Single<Double> {
//        return Single.create { emitter ->
//            val response = restaurantApi.getMoney().execute()
//
//            if (!response.isSuccessful) {
//                emitter.onError(Exception())
//            } else {
//
//                val data = response.body()
//                if (data != null) {
//                    emitter.onSuccess(data)
//                }
//            }
//        }
//    }
//
//    fun getAllUsers(): Single<List<String>> {
//        return Single.create { emitter ->
//            val result = restaurantApi.getAllUsers().execute()
//
//            if (result.isSuccessful) {
//                result.body()?.let { users -> emitter.onSuccess(users) }
//            } else {
//                emitter.onError(Exception("Error"))
//            }
//        }
//    }
}