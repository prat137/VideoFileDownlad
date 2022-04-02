package com.example.demoapp.utils.extension

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * dd MM yyyy
 *
 * @param updateDate date in string format
 * @return date format as a string
 */
fun getDateInLong(updateDate: String): Long {
    val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    //sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date?
    date = sdf.parse(updateDate)
    return date!!.time
}

/**
 * dd MM yyyy
 *
 * @param updateDate date in string format
 * @return date format as a string
 */
fun getDateEndDate(updateDate: String): Long {
    val sdf = SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.getDefault())
    //sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date?
    date = sdf.parse(updateDate)
    return date!!.time
}


fun getDateInDayFormat(time: Long): String {
    var date = ""
    try {
        val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        date = sdf.format(Date(time))
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date
}

fun getDateInRFC3339Format(time: Long): String {
    var date = ""
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        date = sdf.format(Date(time))
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date
}


fun getDateInMDYFormat(time: Long): String {
    var date = ""
    try {
        val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        date = sdf.format(Date(time))
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date
}

/**
 * Method to get date in dd/MM/yyyy HH:mm format
 *
 * @param time timestamp in long datatype
 * @return date format as a string
 */
fun getDateIn24HrsFormatInUTC(time: Long): String {
    var date = ""
    try {
        val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        date = sdf.format(Date(time))
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date
}

fun convertOneDateToAnother(dateStr: String): String {
    val srcDf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    srcDf.timeZone = TimeZone.getTimeZone("GMT")
    // parse the date string into Date object
    // parse the date string into Date object
    val date: Date? = srcDf.parse(dateStr)

    val destDf: DateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    destDf.timeZone = TimeZone.getDefault()
    // format the date into another format
    // format the date into another format
    return destDf.format(date!!)
}

/**
 * This method is give current timestamp
 * @return current time into millisecond
 */
fun currentTimeStamp(): String {
    return getDateInDayFormat(Date().time)
}

/**
 * Method to get first day of week
 *
 * @return date format as a string
 */
fun getFirstDayOfWeek(): String {
    val c1 = Calendar.getInstance()
    //first day of week
    c1[Calendar.DAY_OF_WEEK] = c1.firstDayOfWeek
    return getDateInDayFormat(c1.timeInMillis)
}

/**
 * Method to get last day of week
 *
 * @return date format as a string
 */
fun getLastDayOfWeek(): Long {
    val c1 = Calendar.getInstance()
    //first day of week
    c1[Calendar.DAY_OF_WEEK] = 7
    c1.set(Calendar.HOUR_OF_DAY, 24)
    c1.set(Calendar.MINUTE, 0)
    c1.set(Calendar.SECOND, 0)
    return c1.timeInMillis
}

/**
 * Method to get first day of month
 *
 * @return date format as a string
 */
fun getFirstDayOfMonth(): String {
    val calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
    return getDateInDayFormat(calendar.timeInMillis)
}

/**
 * Method to get last day of month
 *
 * @return date format as a string
 */
fun getLastDayOfMonth(): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1
    return getDateInDayFormat(calendar.timeInMillis)
}

/**
 * Method to get first day of month
 *
 * @return date format as a string
 */
fun getTomorrowDate(): String {

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return getDateInDayFormat(calendar.timeInMillis)
}

/**
 * Method to get second day of month
 *
 * @return date format as a string
 */
fun getTomorrowEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 2)
    return getDateInDayFormat(calendar.timeInMillis)
}