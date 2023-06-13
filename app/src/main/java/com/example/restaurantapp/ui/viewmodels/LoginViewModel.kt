package com.example.restaurantapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.restaurantapp.data.model.entities.Account
import com.example.restaurantapp.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val values = Companion

    private val account = Account()

    private val completedFieldsForLogin: MutableSet<String> = mutableSetOf()

    fun setLoginField(value: String, type: String) {
        when (type) {
            USERNAME_VALUE -> {
                account.username = value
            }
            PASSWORD_VALUE -> {
                account.password = value
            }
        }

        if (value.isEmpty()) {
            completedFieldsForLogin.remove(type)
        } else {
            completedFieldsForLogin.add(type)
        }
    }

    fun isLoginValid(): Boolean {
        if (completedFieldsForLogin.size == NUMBER_OF_FIELDS) {
            return true
        }
        return false
    }

    fun getLoginCompletedFields() : MutableSet<String> {
        return completedFieldsForLogin
    }

    fun login(): Single<Int> {
        return loginRepository.login(account)
    }

    fun isUserLoggedIn(): Single<Int> {
        return loginRepository.isLoggedIn()
    }

    companion object {
        const val USERNAME_VALUE = "username"
        const val PASSWORD_VALUE = "password"
        val ALL_FIELDS = listOf(USERNAME_VALUE, PASSWORD_VALUE)
        private const val NUMBER_OF_FIELDS = 2
    }
}