package com.example.restaurantapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import com.example.restaurantapp.databinding.FragmnetProfileBinding
import com.example.restaurantapp.ui.activities.BaseActivity
import com.example.restaurantapp.ui.activities.LoginActivity
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmnetProfileBinding>() {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmnetProfileBinding.inflate(inflater, container, false)

        if (rootView == null) {
            rootView = viewBinding!!.root
            isFirstLoaded = true
            onBackPressListener()
        } else {
            isFirstLoaded = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstLoaded) {
            initButton()
            loadUserDetails()
        }
    }

    override fun getTitle(): String {
        return "Profilul meu"
    }

    private fun initButton() {
        binding.buttonUpdateDetails.setOnClickListener {
            (activity as BaseActivity).getFragmentNavigation()
                .replaceFragment(UpdateUserDetailsFragment())
        }

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result) {
                            val intent = Intent(activity, LoginActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                    }, { throwable ->
                        throwable.message?.let { safeThrowable ->
                            Log.e(ProfileFragment::class.java.canonicalName,
                                safeThrowable
                            )
                        }
                    })
        }
    }

    private fun loadUserDetails() {
//        viewModel.getUser()
//            .subscribe(
//                { result ->
//                    binding.textUserName.text = context?.getString(
//                        R.string.user_name,
//                        result.firstName,
//                        result.lastName
//                    )
//
//                }, { throwable ->
//                    throwable.message?.let { safeThrowable ->
//                        Log.e(
//                            ProfileFragment::class.java.canonicalName,
//                            safeThrowable
//                        )
//                    }
//                })
    }


    private fun onBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as MainActivity).onBackPressMain()
        }
    }

    override fun hasTopBar(): Boolean = true
}