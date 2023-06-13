package com.example.restaurantapp.ui.activities

import android.os.Bundle
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.ActivityAdminBinding
import com.example.restaurantapp.ui.fragments.admin.AdminBookingsFragment
import com.example.restaurantapp.ui.fragments.admin.AdminProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : BaseActivity() {

    private var _binding: ActivityAdminBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getFragmentNavigation().replaceFragment(AdminBookingsFragment())

        bottomNavigationCallback()
    }

    private fun bottomNavigationCallback() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_bookings -> {
                    getFragmentNavigation().replaceFragment(AdminBookingsFragment())
                    true
                }
                R.id.page_profile -> {
                    getFragmentNavigation().replaceFragment(AdminProfileFragment())
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