package com.example.demoapp.base

import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.demoapp.ui.home.MainActivity
import com.example.demoapp.R
import com.example.demoapp.datalayer.model.BaseResponse
import com.example.demoapp.datalayer.model.ItemLiveData
import com.example.demoapp.datalayer.storage.AppPref
import com.example.demoapp.utils.extension.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_base.*
import retrofit2.Response


abstract class BaseActivity() : AppCompatActivity() {
    protected abstract fun getLayoutId(): Int?
    private var childViewContainer: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getLayoutId() == null) {
            return
        }

        setContentView(R.layout.activity_base)
        childViewContainer = findViewById(R.id.flBaseContainer)

        addChildViews()
    }

    /**
     * add child view of smart activity
     */
    private fun addChildViews() {
        layoutInflater.inflate(getLayoutId()!!, childViewContainer)
    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent intent with extra parameters.
     */
    fun navigateToDifferentScreen(nextScreenIntent: Intent) {
        navigateToDifferentScreen(nextScreenIntent, null, "", false, false, 0, 0)

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent    intent with extra parameters.
     * @param view                view view which will be animate
     * @param sharedElementName   transition name
     * @param finishActivity      boolean set true if want to finish current activity.
     */
    fun navigateToDifferentScreen(
        nextScreenIntent: Intent, view: View, sharedElementName: String, finishActivity: Boolean
    ) {
        navigateToDifferentScreen(
            nextScreenIntent,
            view,
            sharedElementName,
            false,
            finishActivity,
            0,
            0
        )

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent  intent with extra parameters.
     * @param view              view view which will be animate
     * @param sharedElementName transition name
     */
    fun navigateToDifferentScreen(nextScreenIntent: Intent, view: View, sharedElementName: String) {
        navigateToDifferentScreen(nextScreenIntent, view, sharedElementName, false, false, 0, 0)

    }


    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent intent with extra parameters.
     * @param finishActivity   boolean set true if want to finish current activity
     */
    fun navigateToDifferentScreen(nextScreenIntent: Intent, finishActivity: Boolean) {
        navigateToDifferentScreen(nextScreenIntent, null, "", finishActivity, false, 0, 0)

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent    intent with extra parameters.
     * @param finishActivity      boolean set true if want to finish current activity.
     * @param startAnimation      this is start animation use full if isAnimate is true
     * @param endAnimation        this is end animation use full if isAnimate is true
     */
    fun navigateToDifferentScreen(
        nextScreenIntent: Intent, finishActivity: Boolean, startAnimation: Int, endAnimation: Int
    ) {
        navigateToDifferentScreen(
            nextScreenIntent,
            null,
            "",
            true,
            finishActivity,
            startAnimation,
            endAnimation
        )

    }

    /**
     * Common method to navigate in different activity class
     *
     * @param nextScreenIntent    object of Intent
     * @param view                view which will be animate
     * @param sharedElementName   transition name
     * @param isAnimate           boolean for screen animation
     * @param finishActivity      boolean set true if want to finish current activity
     * @param startAnimation      this is start animation use full if isAnimate is true
     * @param endAnimation        this is end animation use full if isAnimate is true
     */
    fun navigateToDifferentScreen(
        nextScreenIntent: Intent,
        view: View?,
        sharedElementName: String,
        isAnimate: Boolean,
        finishActivity: Boolean,
        startAnimation: Int,
        endAnimation: Int
    ) {

        try {
            if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    view,
                    sharedElementName
                )
                startActivity(nextScreenIntent, options.toBundle())
                if (finishActivity) {
                    finish()
                }
            } else {
                startActivity(nextScreenIntent)
                if (isAnimate) {
                    overridePendingTransition(startAnimation, endAnimation)
                }

                if (finishActivity) {
                    finish()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    /**
     * this method is use for replace fragment
     * 1st parameter fragment
     * 2nd layout ot contanint body
     * 3rd parameter back steak or not
     * 4th parameter animation
     * 5th parameter clear stack
     */

    fun replaceFragment(
        fragment: Fragment,
        container: Int,
        needToAddBackStack: Boolean,
        needAnimation: Boolean,
        clearStack: Boolean
    ) {
        val tag = fragment.javaClass.simpleName
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        if (fm.backStackEntryCount > 0 && clearStack) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            if (needAnimation) {
                ft.setCustomAnimations(
                    R.anim.slide_down, R.anim.slide_down, R.anim.slide_up, R.anim.slide_up
                )
            }
        }

        if (needToAddBackStack && !clearStack) {
            ft.replace(container, fragment, tag).addToBackStack(tag).commit()
        } else {
            ft.replace(container, fragment, tag).commit()
        }
    }


    /**
     * Method to show loader on screen
     */
    fun showLoadingView() {
        if (rlProgress != null && pbLoader != null) {
            rlProgress.isClickable = false
            rlProgress.isEnabled = false
            rlProgress.visibility = View.VISIBLE
            pbLoader.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    /**
     * Method to hide loading view
     */
    fun hideLoadingView() {
        if (rlProgress != null && pbLoader != null) {
            rlProgress.isClickable = true
            rlProgress.isEnabled = true
            rlProgress.visibility = View.GONE
            pbLoader.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


    /**
     *  navigate user to main screen
     */
    fun navigateToMainScreen() {
        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        navigateToDifferentScreen(homeIntent, true, R.anim.enter_from_right, R.anim.exit_to_left)
    }

    fun getSuccessResponse(
        itemData: ItemLiveData<Any, Response<Any>>,
        viewModel: BaseViewModel
    ): Boolean {
        itemData.other?.let {
            showToast(getString(R.string.no_internet_found), showInReleaseBuild = true)
        } ?: run {
            itemData.response?.let {
                when {
                    itemData.code == 401 -> logOutUser(viewModel)
                    itemData.code == 500 ->
                        showToast(
                            getString(R.string.internal_server_error),
                            showInReleaseBuild = false
                        )
                    itemData.response!!.isSuccessful ->
                        return true

                    else -> {
                        val errorResponse =
                            Gson().fromJson(
                                itemData.response!!.errorBody()?.string(),
                                BaseResponse::class.java
                            )
                        showToast(
                            if (errorResponse.errors?.isEmpty()!!) "" else errorResponse.errors.toString(),
                            showInReleaseBuild = true
                        )
                    }
                }
            } ?: run {
                showToast(
                    getString(R.string.failed_response_title),
                    showInReleaseBuild = true
                )
            }
        }
        return false
    }

    fun logOutUser(viewModel: BaseViewModel) {
        if (isNetworkAvailable()) {
            val token = AppPref.getValue(PARAM_TOKEN, "").toString()
            viewModel.requestForLogout(token)
        }
        clearPrefs()
    }

    /**
     *  for clear share preference
     */
    private fun clearPrefs() {
        AppPref.clear()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        navigateToDifferentScreen(intent, true, R.anim.enter_from_right, R.anim.exit_to_left)
    }


    /**
     *  for manage permission
     */
    fun managePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        var count = 0
        for (element in grantResults) {
            if (element == PackageManager.PERMISSION_GRANTED) {
                count++
            }
        }
        if (count == grantResults.size) {
            return true
        } else {
            var isSkip = false
            for (element in grantResults) {
                if (element != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermission(permissions.toString())) {
                        showDialogWhenDeniedPermission(getString(R.string.location_permission_title),
                            getString(R.string.location_permission_message),
                            View.OnClickListener {
                                openSettingScreen(requestCode)
                            },
                            View.OnClickListener {
                                isSkip = true
                            })
                        break
                    }
                }
            }
            return isSkip
        }
    }


    /**
     *  for manage permission
     */
    fun manageStoragePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        var count = 0
        for (element in grantResults) {
            if (element == PackageManager.PERMISSION_GRANTED) {
                count++
            }
        }
        if (count == grantResults.size) {
            return true
        } else {
            var isSkip = false
            for (element in grantResults) {
                if (element != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermission(permissions.toString())) {
                        showDialogWhenDeniedPermission(getString(R.string.storage__title),
                            getString(R.string.storage_permission_message),
                            View.OnClickListener {
                                openSettingScreen(requestCode)
                            },
                            View.OnClickListener {
                                isSkip = true
                            })
                        break
                    }
                }
            }
            return isSkip
        }
    }

    /**
     *  check gps is enable or not
     */
    fun isLocationEnabled(): Boolean {
        val locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }




}