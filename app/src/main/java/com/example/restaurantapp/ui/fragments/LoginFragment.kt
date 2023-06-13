package com.example.restaurantapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.restaurantapp.databinding.FragmentLoginBinding
import com.example.restaurantapp.ui.activities.AdminActivity
import com.example.restaurantapp.ui.activities.BaseActivity
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class LoginFragment : ValidatorFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()

    private val disposable = CompositeDisposable()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)

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

        binding.buttonSingIn.setOnClickListener {
            loginButtonClicked()
        }

        binding.buttonSingUp.setOnClickListener {
            (activity as BaseActivity).getFragmentNavigation()
                .replaceFragment(SignupFragment())
        }
    }

    private fun loginButtonClicked() {
        if (!viewModel.isLoginValid()) {
            validate(viewModel.values.ALL_FIELDS, viewModel.getLoginCompletedFields())
            return
        }

        disposable.add(viewModel.login()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    when (result) {
                        0 -> {
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(activity, AdminActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            Toast.makeText(context, "Eroare", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                { throwable ->
                    Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
                }
            ))
    }

    private fun addValidationListeners() {
        addValidationListener(binding.textInputLayoutUsername, viewModel.values.USERNAME_VALUE)
        { x: String, y: String -> viewModel.setLoginField(x, y) }
        addValidationListener(binding.textInputLayoutPassword, viewModel.values.PASSWORD_VALUE)
        { x: String, y: String -> viewModel.setLoginField(x, y) }
    }

    fun onBackPressed() {
        activity?.finish()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun hasTopBar(): Boolean = false
}