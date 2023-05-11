package com.example.restaurantapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentUpdateUserDetailsBinding
import com.example.restaurantapp.ui.activities.BaseActivity
import com.example.restaurantapp.ui.viewmodels.UpdateProfileViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateUserDetailsFragment : ValidatorFragment<FragmentUpdateUserDetailsBinding>() {

    private val viewModel by viewModels<UpdateProfileViewModel>()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentUpdateUserDetailsBinding.inflate(inflater, container, false)

        if (rootView == null) {
            rootView = viewBinding!!.root
            isFirstLoaded = true
        } else {
            isFirstLoaded = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isFirstLoaded) {
            loadUserDetails()
            binding.buttonUpdateDetails.setOnClickListener {
                validateOnCheckClicked()
            }
            addValidationListeners()
        }
    }

    override fun getTitle(): String {
        return "Modifica datele"
    }

    private fun loadUserDetails() {
        viewModel.getUser()
            .subscribe(
                { userDetails ->
                    binding.editTextFirstName.setText(userDetails.firstName)
                    binding.editTextLastName.setText(userDetails.lastName)
                    binding.editTextPhone.setText(userDetails.phone)

                }, { throwable ->
                    throwable.message?.let { safeThrowable ->
                        Log.e(
                            ProfileFragment::class.java.canonicalName,
                            safeThrowable
                        )
                    }
                })
    }

    private fun validateOnCheckClicked() {
        if (!viewModel.isValid()) {
            validate(viewModel.values.ALL_FIELDS, viewModel.getCompletedFields())
            return
        }

        viewModel.updateUser()
            .subscribe(
                { result ->
                    if (result == true) {
                        (activity as BaseActivity).getFragmentNavigation()
                            .replaceFragment(ProfileFragment())
                    }
                }, { throwable ->
                    throwable.message?.let { safeThrowable ->
                        Log.e(
                            UpdateUserDetailsFragment::class.java.canonicalName,
                            safeThrowable
                        )
                    }
                })
    }

    override fun getTextInputsMap(): Map<String, TextInputLayout> {
        val map = HashMap<String, TextInputLayout>()

        map[viewModel.values.FIRST_NAME] = binding.textInputFirstName
        map[viewModel.values.LAST_NAME] = binding.textInputLastName
        map[viewModel.values.PHONE] = binding.textInputPhone

        return map
    }

    private fun addValidationListeners() {
        addValidationListener(binding.textInputLastName, viewModel.values.LAST_NAME)
        { x: String, y: String -> viewModel.setUserFields(x, y) }
        addValidationListener(binding.textInputFirstName, viewModel.values.FIRST_NAME)
        { x: String, y: String -> viewModel.setUserFields(x, y) }
        addValidationListener(binding.textInputPhone, viewModel.values.PHONE)
        { x: String, y: String -> viewModel.setUserFields(x, y) }
    }

    override fun hasTopBar(): Boolean = true
}