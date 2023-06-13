package com.example.restaurantapp.ui.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentHomeBinding
import com.example.restaurantapp.ui.activities.BaseActivity
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.fragments.BaseFragment
import com.example.restaurantapp.ui.viewmodels.BookingsViewModel
import com.example.restaurantapp.ui.viewmodels.ProfileViewModel
import com.example.restaurantapp.utils.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModelProfile by viewModels<ProfileViewModel>()

    private val viewModelBookings by viewModels<BookingsViewModel>()

    private val disposable = CompositeDisposable()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)

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
            loadUserDetails()
            loadNextBooking()
            initButtons()
        }
    }

    private fun initButtons() {
        binding.buttonMakeTransaction.setOnClickListener {
            if (viewModelProfile.checkUserDetails()) {
                (activity as BaseActivity).getFragmentNavigation()
                    .replaceFragment(MakeBookingFragment())
            } else {
                Toast.makeText(context, "Va rugam sa va completati datele!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserDetails() {
        disposable.add(viewModelProfile.getUser()
            .subscribe(
                { userDetails ->
                    if (viewModelProfile.checkUserDetails()) {
                        binding.textUserName.text = context?.getString(
                            R.string.user_name,
                            userDetails.firstName,
                            userDetails.lastName
                        )
                    } else {
                        binding.textUserName.text = "Va rugam sa va completati datele!"
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

    private fun loadNextBooking() {
        disposable.add(viewModelBookings.getFirstBooking()
            .subscribe(
                { booking ->
                    binding.textViewNext.text = context?.getString(R.string.you_have_res)
                    binding.textViewNextInfo.visibility = View.VISIBLE
                    binding.textViewNextInfo.text = DateFormatter.getDateTimeFromMillis(booking.time)

                }, { throwable ->
                    binding.textViewNext.text = context?.getString(R.string.no_res)
                    binding.textViewNextInfo.visibility = View.INVISIBLE
                    throwable.message?.let { safeThrowable ->
                        Log.e(
                            ProfileFragment::class.java.canonicalName,
                            safeThrowable
                        )
                    }
                }))
    }

    override fun getTitle(): String {
        return "Acasa"
    }

    private fun onBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as MainActivity).onBackPressMain()
        }
    }

    override fun hasTopBar(): Boolean = true

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}