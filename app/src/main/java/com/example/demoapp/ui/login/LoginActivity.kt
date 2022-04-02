package com.example.demoapp.ui.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.demoapp.BuildConfig
import com.example.demoapp.R
import com.example.demoapp.base.BaseActivity
import com.example.demoapp.datalayer.model.ItemLiveData
import com.example.demoapp.datalayer.model.LoginResponse
import com.example.demoapp.utils.extension.hideKeyboard
import com.example.demoapp.utils.extension.isNetworkAvailable
import com.example.demoapp.utils.extension.showToast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel

    override fun getLayoutId(): Int? {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        buttonClickListener()
    }

    /**
     * for initialize variable
     */
    private fun initView() {
        viewModel =
            ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        edtPassword.transformationMethod = PasswordTransformationMethod()
        setDataObserver()

        if (BuildConfig.DEBUG) {
            /*edtEmail.setText("dimple@mailinator.com")
            edtPassword.setText("Dimple@123")*/
            /*edtEmail.setText("prashantmvora97@gmail.com")
            edtPassword.setText("Password@123")*/
        }
    }

    /**
     * for register on click listener of views
     */
    private fun buttonClickListener() {
        btnLogin.setOnClickListener(this)
        tvForgotPassword.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLogin -> {
                v.hideKeyboard()
                doLogin()
            }
        }
    }

    /**
     *  for manage login
     */
    private fun doLogin() {
        if (validateLoginUserData()) {
            requestForLogIn()
        } else {
            showToast(
                getString(R.string.invalid_username_or_password),
                showInReleaseBuild = true
            )
        }
    }

    /**
     * for validate login user data
     */
    private fun validateLoginUserData(): Boolean {
        if (edtEmail.text?.trim().toString().isEmpty() || edtPassword.text?.trim().toString()
                .isEmpty()
        ) {
            return false
        }
        return true
    }

    /**
     * for observer api data
     */
    private fun setDataObserver() {
        viewModel.loginResponse().observe(this, Observer { response ->
            manageLoginResponse(response)
        })
    }

    /**
     *  request for log in data
     */
    private fun requestForLogIn() {
        showLoadingView()
        if (isNetworkAvailable()) {
            viewModel.requestForLogIn(
                edtEmail.text?.trim().toString(),
                edtPassword.text?.trim().toString()
            )
        } else {
            hideLoadingView()
            showToast(getString(R.string.no_internet_found), showInReleaseBuild = true)
        }
    }

    /**
     *  for manage api response of login
     */
    private fun manageLoginResponse(itemLiveData: ItemLiveData<Any, Response<Any>>) {
        hideLoadingView()
        if (getSuccessResponse(itemLiveData, viewModel)) {
            val response = itemLiveData.response as Response<LoginResponse>
            if (!response.body()!!.token.isNullOrEmpty()) {
                navigateToMainScreen()
            }

        }
    }

}