package com.example.demoapp.utils.extension


/**
 * Method to log login Event in fabric event
 *//*

fun loginEvent(isLogin: Boolean, methode: String) {
    if (BuildConfig.FABRIC_EVENT_ENABLE) {
        try {
            Answers.getInstance().logLogin(
                LoginEvent()
                    .putMethod(methode)
                    .putSuccess(isLogin)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


*/
/**
 * Method to log login Event in fabric event
 *//*

fun installEvent(isLogin: Boolean) {
    val deviceInfo: HashMap<String, Any> = getDeviceInfo()
    if (BuildConfig.FABRIC_EVENT_ENABLE) {
        try {
            Answers.getInstance().logCustom(
                CustomEvent("Install")
                    .putCustomAttribute("isInstall", isLogin.toString())
                    .putCustomAttribute("deviceManufacture", deviceInfo[PARAM_DEVICE_MANUFACTURER].toString())
                    .putCustomAttribute("deviceModel", deviceInfo[PARAM_DEVICE_MODEL].toString())
                    .putCustomAttribute("osVersion", deviceInfo[PARAM_OS_VERSION].toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
*/
