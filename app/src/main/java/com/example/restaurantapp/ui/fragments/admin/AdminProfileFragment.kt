package com.example.restaurantapp.ui.fragments.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import com.example.restaurantapp.databinding.FragmentProfileAdminBinding
import com.example.restaurantapp.ui.activities.AdminActivity
import com.example.restaurantapp.ui.activities.LoginActivity
import com.example.restaurantapp.ui.fragments.BaseFragment
import com.example.restaurantapp.ui.fragments.user.ProfileFragment
import com.example.restaurantapp.ui.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


@AndroidEntryPoint
class AdminProfileFragment : BaseFragment<FragmentProfileAdminBinding>() {

    private val viewModel by viewModels<ProfileViewModel>()

    private val disposable = CompositeDisposable()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentProfileAdminBinding.inflate(inflater, container, false)

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
        }
    }

    private fun initButton() {
        binding.buttonLogout.setOnClickListener {
            disposable.add(viewModel.logout()
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
                            Log.e(
                                ProfileFragment::class.java.canonicalName,
                                safeThrowable
                            )
                        }
                    }))
        }
    }

    override fun getTitle(): String {
        return "Profilul meu"
    }

    override fun hasTopBar() = true

    private fun onBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as AdminActivity).onBackPressMain()
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}