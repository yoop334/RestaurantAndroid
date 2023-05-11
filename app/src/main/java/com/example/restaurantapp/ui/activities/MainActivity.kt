package com.example.restaurantapp.ui.activities

import android.os.Bundle
import com.example.restaurantapp.ui.fragments.HomeFragment
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.ActivityMainBinding
import com.example.restaurantapp.ui.fragments.BookingsFragment
import com.example.restaurantapp.ui.fragments.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getFragmentNavigation().replaceFragment(HomeFragment())

        bottomNavigationCallback()
    }

    private fun bottomNavigationCallback() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    getFragmentNavigation().replaceFragment(HomeFragment())
                    true
                }
                R.id.page_bookings -> {
                    getFragmentNavigation().replaceFragment(BookingsFragment())
                    true
                }
                R.id.page_profile -> {
                    getFragmentNavigation().replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun onBackPressMain() {
        binding.bottomNavigation.selectedItemId = R.id.page_home
    }
}