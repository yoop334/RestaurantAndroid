package com.example.restaurantapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.restaurantapp.databinding.FragmentLoginBinding
import com.example.restaurantapp.databinding.FragmentSigupBinding
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.viewmodels.LoginViewModel
import com.example.restaurantapp.ui.viewmodels.SignupViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class SignupFragment : ValidatorFragment<FragmentSigupBinding>() {

    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentSigupBinding.inflate(inflater, container, false)

        if (rootView == null) {
            rootView = viewBinding!!.root
            isFirstLoaded = true
        } else {
            isFirstLoaded = false
        }
    }

    override fun getTitle(): String = ""

    override fun getTextInputsMap(): Map<String, TextInputLayout> {
        val map = HashMap<String, TextInputLayout>()

        map[viewModel.values.USERNAME_VALUE] = binding.textInputLayoutUsername
        map[viewModel.values.PASSWORD_VALUE] = binding.textInputLayoutPassword

        return map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addValidationListeners()

        binding.buttonCreateAccount.setOnClickListener {
            createAccountClicked()
        }
    }

    private fun createAccountClicked() {
        if (!viewModel.isAccountValid()) {
            validateOnCreateClicked()
            return
        }

        viewModel.createUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result == true) {
                        loginUser()
                    }
                },
                { throwable ->
                    Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun loginUser() {
        viewModel.login()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result == true) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    }
                },
                { throwable ->
                    Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun validateOnCreateClicked() {
        var isValid: Boolean
        for (field in viewModel.values.ALL_FIELDS) {
            isValid = viewModel.getCompletedFields().contains(field)

            when (field) {
                LoginViewModel.USERNAME_VALUE -> {
                    setValidationError(binding.textInputLayoutUsername, isValid)
                }
                LoginViewModel.PASSWORD_VALUE -> {
                    setValidationError(binding.textInputLayoutPassword, isValid)
                }
            }
        }
    }

    private fun addValidationListeners() {
        addValidationListener(binding.textInputLayoutUsername, viewModel.values.USERNAME_VALUE)
        addValidationListener(binding.textInputLayoutPassword, viewModel.values.PASSWORD_VALUE)
    }

    private fun setValidationError(textInputLayout: TextInputLayout, isValid: Boolean) {
        if (isValid) {
            textInputLayout.isErrorEnabled = false
        } else {
            textInputLayout.error = "Add " + textInputLayout.hint
        }
    }

    private fun addValidationListener(textInputLayout: TextInputLayout, field: String) {
        val editText = textInputLayout.editText ?: return

        editText.addTextChangedListener { editable ->
            val value = editable.toString()

            if (value.isEmpty()) {
                textInputLayout.error = "Add " + textInputLayout.hint
            } else {
                textInputLayout.isErrorEnabled = false
            }
            viewModel.setLoginField(value, field)
        }
    }

    override fun hasTopBar(): Boolean = false
}