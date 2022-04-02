package com.example.demoapp.utils.extension

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.demoapp.R



private var dialog: Dialog? = null

/**
 * Method to check requested permissions are granted or not.
 * @param permissions requested permissions array
 * @return returns true if all requested permissions are granted else false.
 */
fun Context.hasPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

/**
 * Method to check requested single permission are granted or not.
 * @param permission requested permission
 * @return returns true if requested permission is granted else false.
 */
fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * Gets whether you should show UI with rationale for requesting permissions.
 *
 * @param permissions The permissions your app wants to request.
 * @return Whether you can show permission rationale UI.
 */
fun shouldShowRequestPermissions(fragment: Fragment, permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                return false
            }
        }
    }
    return true
}

/**
 * Gets whether you should show UI with rationale for requesting permissions.
 *
 * @param permissions The permissions your app wants to request.
 * @return Whether you can show permission rationale UI.
 */
fun Activity.shouldShowRequestPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!this.shouldShowRequestPermissionRationale(permission)) {
                return false
            }
        }
    }
    return true
}


/**
 * This is public static method  to check should show permission dialog or not by calling
 * shouldShowRequestPermissionRationale of  ActivityCompat java class.
 * @param permission requested permission in string
 * @return true if should show permission else false if user has selected "Never ask" option earlier.
 */
fun Activity.shouldShowRequestPermission(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

/**
 * This is public static method  to check should show permission dialog or not by calling
 * shouldShowRequestPermissionRationale of  ActivityCompat java class.
 * @param permission requested permission in string
 * @return true if should show permission else false if user has selected "Never ask" option earlier.
 */
fun Fragment.shouldShowRequestPermission(permission: String): Boolean {
    return shouldShowRequestPermissionRationale(permission)
}

/**
 * Method ro request permission with list of permissions.
 *
 * @param permissions The permissions your app wants to request.
 * @param reqId reqId which will return in call back of permission in  onRequestPermissionsResult method in activity
 */
fun Activity.requestPermission(permissions: Array<String>, reqId: Int) {
    ActivityCompat.requestPermissions(this, permissions, reqId)
}

/**
 * Method ro request permission with list of permissions.
 *
 * @param permissions The permissions your app wants to request.
 * @param reqId reqId which will return in call back of permission in  onRequestPermissionsResult method in activity
 */
fun Fragment.requestPermissionForFragment(permissions: Array<String>, reqId: Int) {
    requestPermissions(permissions, reqId)
}

/**
 * Method ro request permission with list of permissions.
 *
 * @param fragment  instance of fragment
 * @param permissions The permissions your app wants to request.
 * @param reqId reqId which will return in call back of permission in  onRequestPermissionsResult method in fragment class.
 */
fun Fragment.requestPermission(permissions: Array<String>, reqId: Int) {
    requestPermissions(permissions, reqId)
}

/**
 * This method shows permission dialog with permission detail and purpose of particular permission.
 * @param titleMessage titleMessage
 * @param purposeMessage purposeMessage
 * @param allowListener  call back method for allow button clicked
 * @param skipListener call skip method for allow button clicked
 */
fun Activity.showDialogWhenDeniedPermission(
    titleMessage: String, purposeMessage: String,
    allowListener: View.OnClickListener, skipListener: View.OnClickListener
) {
    dialog = Dialog(this)
    dialog!!.setContentView(R.layout.dialog_external_permission)


    val windowManagerLayoutParams = WindowManager.LayoutParams()
    val window = dialog!!.window

    if (window != null) {
        windowManagerLayoutParams.copyFrom(window.attributes)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //This makes the dialog take up the full width
        windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowManagerLayoutParams.width = SCREEN_WIDTH - SCREEN_WIDTH / 10
        window.attributes = windowManagerLayoutParams
    }

    val tvAllow = dialog!!.findViewById<AppCompatTextView>(R.id.tvAllow)
    val tvSkip = dialog!!.findViewById<AppCompatTextView>(R.id.tvSkip)
    val tvPermissionTitle = dialog!!.findViewById<AppCompatTextView>(R.id.tvPermissionTitle)
    val tvPurposePermissionMsg =
        dialog!!.findViewById<AppCompatTextView>(R.id.tvPurposePermissionMsg)

    tvPurposePermissionMsg.text = purposeMessage
    tvPermissionTitle.text = titleMessage

    dialog!!.setCancelable(false)

    tvAllow.setOnClickListener {
        dialog!!.dismiss()
        allowListener.onClick(it)
    }
    tvSkip.setOnClickListener {
        dialog!!.dismiss()
        skipListener.onClick(it)
    }
    dialog!!.show()
}


/**
 * Method to dismissed previously visible dialog before showing new dialog.
 */
fun hideDialogWhenDeniedPermission() {
    try {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
        dialog = null
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * Method to get required permission string from permission string array
 * @param permissions String array of required permission
 * @return returns empty if not required permission else required permission string.
 */
private fun Context.getRequiredPermissionString(permissions: Array<String>): String {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return permission
        }
    }
    return ""
}


/**
 * Method to check user checked "Never asked" option or not.
 * @param permissions String array of required permission
 * @return returns true if neverAsked selected else returns false.
 */
fun Activity.hasPermissionDenied(permissions: Array<String>): Boolean {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val permission = getRequiredPermissionString(permissions)
        !TextUtils.isEmpty(permission) && shouldShowRequestPermissionRationale(permission)
    } else {
        false
    }
}
