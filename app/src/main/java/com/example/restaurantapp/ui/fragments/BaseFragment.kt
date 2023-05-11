package com.example.restaurantapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.restaurantapp.databinding.ViewTopBarBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected var viewBinding: VB? = null

    protected val binding get() = viewBinding!!

    protected var rootView: View? = null

    protected var isFirstLoaded = false

    private lateinit var topBarBinding: ViewTopBarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onCreateViewBinding(inflater, container)

        if (hasTopBar()) {
            topBarBinding = ViewTopBarBinding.bind(binding.root)
            initTitle()
        }

        return rootView
    }

    private fun initTitle() {
        topBarBinding.textTitle.text = getTitle()
    }

    abstract fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?)

    abstract fun getTitle(): String

    abstract fun hasTopBar(): Boolean
}