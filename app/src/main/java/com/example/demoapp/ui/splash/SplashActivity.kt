package com.example.demoapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.demoapp.ui.home.MainActivity
import com.example.demoapp.R
import com.example.demoapp.base.BaseActivity
import com.example.demoapp.datalayer.storage.AppPref
import com.example.demoapp.ui.login.LoginActivity
import com.example.demoapp.ui.splash.viewmodel.SplashViewModelFactory
import com.example.demoapp.ui.splash.viewmodel.SplashViewModel
import com.example.demoapp.utils.extension.PARAM_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int? {
        return R.layout.activity_splash
    }


    private lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    /**
     * for initialize variable
     */
    private fun initView() {
        viewModel = ViewModelProviders.of(
            this,
            SplashViewModelFactory(this)
        ).get(SplashViewModel::class.java)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            navigateUserToOtherScreen()
        }


    }

    /**
     * fro navigate user to other screen
     * */
    private fun navigateUserToOtherScreen() {
        if (AppPref.getValue(PARAM_TOKEN, "")!!.isEmpty()) {
            navigateToHomeScreen()
        } else {
            navigateToHomeScreen()
        }
    }

    /**
     *  navigate user to login screen
     */
    private fun navigateUserToLoginScreen() {
        val homeIntent = Intent(this, LoginActivity::class.java)
        navigateToDifferentScreen(homeIntent, true, R.anim.enter_from_right, R.anim.exit_to_left)
    }

    /**
     * Method to navigate to HomeActivity screen.
     */
    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        navigateToDifferentScreen(
            getNotificationExtraData(intent),
            true,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )
    }

    /**
     * Method to get notification data from intent extra
     */
    private fun getNotificationExtraData(activityIntent: Intent): Intent {
        activityIntent.putExtra(
            "action", if (intent.hasExtra("action")) intent.getStringExtra("action") else null
        )
        activityIntent.putExtra(
            "job_id",
            if (intent.hasExtra("job_id")) intent.getStringExtra("job_id") else null
        )
        return activityIntent
    }

}