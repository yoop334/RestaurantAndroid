package com.example.restaurantapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentSplashBinding
import com.example.restaurantapp.ui.activities.AdminActivity
import com.example.restaurantapp.ui.activities.BaseActivity
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class SplashFragment : Fragment(), Animation.AnimationListener {
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    private val disposable = CompositeDisposable()

    private lateinit var animation: Animation

    private var _binding: FragmentSplashBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation = AnimationUtils.loadAnimation(view.context, R.anim.text_anim)
        binding.textViewLogo.startAnimation(animation)

        animation.setAnimationListener(this)
    }

    override fun onAnimationStart(p0: Animation?) {
        // nothing
    }

    override fun onAnimationEnd(p0: Animation?) {

        disposable.add(viewModel.isUserLoggedIn()
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
                {
                    if (activity is BaseActivity) {
                        (activity as BaseActivity).getFragmentNavigation()
                            .replaceFragment(LoginFragment(), true)
                    }
                }
            ))
    }

    override fun onAnimationRepeat(p0: Animation?) {
        // nothing
    }

    fun onBackPressed() {
        activity?.finish()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}