package com.example.restaurantapp.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.fragments.LoginFragment
import com.example.restaurantapp.ui.fragments.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getFragmentNavigation().replaceFragment(SplashFragment())
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragments: List<Fragment> = supportFragmentManager.fragments

        val handled = false
        fragments.forEach { fragment ->
            if (fragment is LoginFragment) {
                fragment.onBackPressed()
            } else if (fragment is SplashFragment) {
                fragment.onBackPressed()
            }
        }

        if (!handled) {
            super.onBackPressed()
        }
    }
}