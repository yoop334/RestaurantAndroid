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
class UpdateProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val values = Companion

    private var userDetails = UserDetails()

    private val completedFields: MutableSet<String> = mutableSetOf()

    fun setUserFields(value: String, type: String) {
        when (type) {
            FIRST_NAME -> {
                userDetails.firstName = value
            }
            LAST_NAME -> {
                userDetails.lastName = value
            }
            PHONE -> {
                userDetails.phone = value
            }
        }

        if (value.isEmpty()) {
            completedFields.remove(type)
        } else {
            completedFields.add(type)
        }
    }

    fun updateUser(): Single<Boolean> {
        return userRepository.updateUser(userDetails)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

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

    fun isValid(): Boolean {
        if (completedFields.size == NUMBER_OF_FIELDS) {
            return true
        }
        return false
    }

    fun getCompletedFields(): MutableSet<String> {
        return completedFields
    }

    companion object {
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val PHONE = "phone"
        val ALL_FIELDS = listOf(FIRST_NAME, LAST_NAME, PHONE)
        private const val NUMBER_OF_FIELDS = 3
    }
}