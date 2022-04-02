package com.example.demoapp.utils.extension


import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.SpannableString
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.demoapp.BuildConfig
import com.example.demoapp.R
import com.example.demoapp.utils.view.*
import com.google.android.material.textfield.TextInputEditText

import io.reactivex.Observable
import java.util.regex.Pattern

var providerMap: HashMap<String, Int> = HashMap()
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}


fun Context.isNetworkAvailableObservable(): Observable<Boolean> {
    return Observable.just(this.isNetworkAvailable())
}


fun Context.showToast(
    text: String,
    toastLength: Int = Toast.LENGTH_SHORT,
    showInReleaseBuild: Boolean = false
) {
    if (!showInReleaseBuild) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, text, toastLength).show()
        }
    } else {
        Toast.makeText(this, text, toastLength).show()
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * This method is used set window dimensions
 */
fun Context.setWindowDimensions() {
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    SCREEN_WIDTH = size.x
    SCREEN_HEIGHT = size.y

}

/**
 * Method to navigate setting screen
 */
fun Activity.openSettingScreen(requestCode: Int) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    if (requestCode > 0) {
        startActivityForResult(intent, requestCode)
    } else {
        startActivity(intent)
    }
}

fun Context.setSpanForPurpleWhite(string2: String, string3: String): SpannableString {
    val spannedScore =
        spannable { color(ContextCompat.getColor(this, R.color.colorApp), string2) }
    val spannedBaseScore = spannable { color(Color.WHITE, string3) }
    return spannedScore.plus(WHITE_SPACE).plus(spannedBaseScore)
}


/*fun Activity.getHahKeyFromFacebook() {

    val info: PackageInfo
    try {
        info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        if (Build.VERSION.SDK_INT >= 28) {
            for (signature in info.signingInfo.apkContentsSigners) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                //String something = new String(Base64.encodeBytes(md.digest()));
                errorLog("hash key", something)
            }
        }
    } catch (e1: PackageManager.NameNotFoundException) {
        errorLog("name not found", e1.toString())
    } catch (e: NoSuchAlgorithmException) {
        errorLog("no such an algorithm", e.toString())
    } catch (e: Exception) {
        errorLog("exception", e.toString())
    }
}*/

fun Context.getSpannableText(
    titleString: String, valueString: String, color: Int = R.color.colorApp
): SpannableString? {
    val spanned1 = spannable {
        normal(titleString)
    }

    val spanned2 = spannable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bold(color(getColor(color), valueString))
        } else {
            bold(color(color, valueString))
        }
    }

    return spanned1.plus(WHITE_SPACE).plus(spanned2)
}

fun getUnderLinedText(titleString: String): SpannableString? {
    return spannable {
        underline(titleString)
    }
}

/**
 * Helper to convert dp to px
 *
 * @param dp
 * @return float
 */
fun dpToPx(dp: Float): Float {
    return dp * (Resources.getSystem().displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * Helper to convert px to sp
 *
 * @param px
 * @return float
 */
fun pxToSp(px: Float): Float {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return px / scaledDensity
}


fun convertToFloat(value: String?): Float {
    return if (value.isNullOrEmpty()) 0f
    else value.toFloat()
}

fun Activity.openWebView(url: String?) {
    if (url.isNullOrEmpty()) return
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (e: Exception) {
    }
}
/*
*//**
 * Method to get device and OS  info in hashMap
 *//*
fun getDeviceInfo(time: Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())): HashMap<String, Any> {
    errorLog("time stamp", ">>>>>>>>>>>" + TimeUnit.SECONDS.toMinutes(time))
    val params = HashMap<String, Any>()
    params[PARAM_OS_TYPE] = PLATFORM
    params[PARAM_OS_VERSION] = Build.VERSION.RELEASE
    params[PARAM_APP_VERSION] = BuildConfig.VERSION_NAME
    params[PARAM_DEVICE_MANUFACTURER] = Build.MANUFACTURER
    params[PARAM_DEVICE_MODEL] = Build.MODEL
    //TODO check at release
    params[PARAM_DEVICE_ID] = ""
    params[PARAM_TS] = time

    return params

}*/

fun TextInputEditText.checkPassword(): Boolean {
    //^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$
    val password = Pattern.compile("(^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?!.* ).{8,})")
    if (!password.matcher(this.text?.let { it.toString() }).matches()) {
        return false
    }
    return true
}


/**
 * Method to showNotification with Image/Bitmap. if dont want to show bitmap then it will be null initially
 *
 * @param title           notification Title
 * @param message         notification extra message
 * @param intent          pending intent
 * @param context         context
 * @param channelId       channelID (ANDROID/PUSH)
 * @param NOTIFICATION_ID NOTIFICATION_ID
 */
fun showNotification(
    context: Context,
    channelId: String,
    NOTIFICATION_ID: Int,
    title: String,
    message: String,
    intent: Intent?
) {

    val mNotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val color = ContextCompat.getColor(context, R.color.colorPrimary)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        // Configure the notification channel.
        notificationChannel.description = context.getString(R.string.app_name)
        notificationChannel.enableLights(true)
        //            notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        mNotificationManager.createNotificationChannel(notificationChannel)
    }
    val mBuilder = NotificationCompat.Builder(context, channelId)
    mBuilder.setSmallIcon(R.mipmap.ic_launcher)
    mBuilder.setContentTitle(title).setContentText(message)
    mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
    mBuilder.setAutoCancel(true)
    mBuilder.setSound(alarmSound)
    mBuilder.setDefaults(Notification.DEFAULT_ALL)
    mBuilder.color = color;
    mBuilder.setContentIntent(contentIntent)
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
}

fun Context.shareApp(shareText: String, passCode: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT, "$shareText $passCode"
    )
    sendIntent.type = "text/plain"
    startActivity(Intent.createChooser(sendIntent, "Share"))
}

fun Context.setSpanColor(string1: String, string2: String): SpannableString? {
    val spanned1 = spannable { bold(string1) }
    val spanned2 = spannable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color(getColor(R.color.colorPrimary), string2)
        } else {
            color((R.color.colorPrimary), string2)
        }
    }
    return spanned1.plus(spanned2)
}

fun Context.setRedSpanColor(string1: String, string2: String): SpannableString? {
    val spanned1 = spannable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color(getColor(R.color.color_red), string1)
        } else {
            color((R.color.color_red), string1)
        }
    }

    return spanned1.plus(string2)
}