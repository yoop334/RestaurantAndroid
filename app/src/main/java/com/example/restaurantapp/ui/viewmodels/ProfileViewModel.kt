package com.example.restaurantapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.restaurantapp.data.model.entities.UserDetails
import com.example.restaurantapp.data.repositories.LoginRepository
import com.example.restaurantapp.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private var userDetails: UserDetails? = null

    fun getUser(): Single<UserDetails> {
        return Single.create { emitter ->
            userRepository.getUserDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    userDetails = user
                    emitter.onSuccess(user)
                }, { throwable ->
                    emitter.onError(throwable)
                })
        }
    }

    fun checkUserDetails(): Boolean {
        return userDetails?.firstName != null
//        userDetails?.phone
//        userDetails?.let { safeUserDetails ->
//            return@let safeUserDetails.firstName == null || safeUserDetails.lastName == null ||
//                    safeUserDetails.phone == null
//        }
//        return false
    }


    fun logout(): Single<Boolean> {
        return loginRepository.logout()
    }
}