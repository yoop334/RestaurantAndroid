package com.example.restaurantapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.navigation.FragmentNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private lateinit var fragmentNavigation: FragmentNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        fragmentNavigation = FragmentNavigation(this)
    }

    fun getFragmentNavigation(): FragmentNavigation {
        return fragmentNavigation
    }
}
