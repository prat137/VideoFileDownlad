package com.example.demoapp.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.demoapp.base.BaseActivity

abstract class BaseFragment : Fragment() {
    protected abstract fun getLayoutId(): Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(getLayoutId(), container, false)
    }

    fun navigateToDifferentScreen(nextScreenIntent: Intent) {
        if (activity != null)
            (activity as BaseActivity).navigateToDifferentScreen(
                nextScreenIntent,
                null,
                "",
                false,
                false,
                0,
                0
            )

    }

    fun navigateToDifferentScreen(
        nextScreenIntent: Intent, view: View, sharedElementName: String, finishActivity: Boolean
    ) {
        if (activity != null)
            (activity as BaseActivity).navigateToDifferentScreen(
                nextScreenIntent, view, sharedElementName,
                false, finishActivity, 0, 0
            )

    }

    fun navigateToDifferentScreen(nextScreenIntent: Intent, view: View, sharedElementName: String) {
        if (activity != null)
            (activity as BaseActivity).navigateToDifferentScreen(
                nextScreenIntent, view, sharedElementName,
                false, false, 0, 0
            )

    }

    fun navigateToDifferentScreen(nextScreenIntent: Intent, finishActivity: Boolean) {
        if (activity != null)
            (activity as BaseActivity).navigateToDifferentScreen(
                nextScreenIntent, null, "", false,
                finishActivity, 0, 0
            )

    }

    fun navigateToDifferentScreen(
        nextScreenIntent: Intent, finishActivity: Boolean, startAnimation: Int, endAnimation: Int
    ) {
        if (activity != null)
            (activity as BaseActivity).navigateToDifferentScreen(
                nextScreenIntent, null, "",
                true, finishActivity, startAnimation, endAnimation
            )
    }


    /**
     * this method is use for replace fragment
     * @param fragment parameter fragment
     * @param container layout ot contanint body
     * @param needToAddBackStack parameter back steak or not
     * @param needAnimation parameter animation
     * @param clearStack parameter clear stack
     */

    fun replaceFragment(
        fragment: Fragment, container: Int, needToAddBackStack: Boolean,
        needAnimation: Boolean, clearStack: Boolean
    ) {
        if (activity != null)
            (activity as BaseActivity).replaceFragment(
                fragment, container, needToAddBackStack,
                needAnimation, clearStack
            )

    }
}