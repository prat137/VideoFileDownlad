package com.example.demoapp.utils.extension

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.IntentSender
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.demoapp.R

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

/**
 * Method to show alert dialog with two/single button
 *
 * @param message               Message of dialog
 * @param posButtonName         Name of positive button
 * @param nagButtonName         Name of negative button
 * @param onPositiveButtonClick call back method of positive button
 * @param onNegativeButtonClick call back method of negative butoon
 */
fun Context.showCustomTwoButtonAlertDialog(
    title: String, message: String, posButtonName: String,
    nagButtonName: String, isOutSideCancelable: Boolean,
    onPositiveButtonClick: DialogInterface.OnClickListener,
    onNegativeButtonClick: DialogInterface.OnClickListener
) {
    this.showCustomTwoButtonAlertDialog(
        title, message, posButtonName, nagButtonName,
        false, isOutSideCancelable, onPositiveButtonClick, onNegativeButtonClick
    )
}

/**
 * Method to show alert dialog with single  positive  button
 *
 * @param message               Message of dialog
 * @param posButtonName         Name of positive button
 * @param onPositiveButtonClick call back method of positive button
 */
fun Context.showCustomTwoButtonAlertDialog(
    title: String, message: String, posButtonName: String,
    isOutSideCancelable: Boolean, onPositiveButtonClick: DialogInterface.OnClickListener
) {
    this.showCustomTwoButtonAlertDialog(
        title, message, posButtonName, "",
        false, isOutSideCancelable, onPositiveButtonClick, null
    )
}

/**
 * Method to show alert dialog with two/single button
 *
 * @param message               Message of dialog
 * @param posButtonName         Name of positive button
 * @param nagButtonName         Name of negative button
 * @param onPositiveButtonClick call back method of positive button
 * @param onNegativeButtonClick call back method of negative butoon
 */
fun Context.showCustomTwoButtonAlertDialog(
    title: String, message: String, posButtonName: String,
    nagButtonName: String, changeButtonColor: Boolean, isOutSideCancelable: Boolean,
    onPositiveButtonClick: DialogInterface.OnClickListener?,
    onNegativeButtonClick: DialogInterface.OnClickListener?
) {
    try {
        val builder: AlertDialog.Builder =
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
            } else {
                AlertDialog.Builder(this, R.style.DialogTheme)
            }
        builder.setMessage(message)
        if (title.isNotEmpty()) {
            builder.setTitle(title)
        } else {
            builder.setTitle(this.resources.getString(R.string.app_name))
        }

        if (onPositiveButtonClick != null) {
            builder.setPositiveButton(posButtonName, onPositiveButtonClick)
        }
        if (onNegativeButtonClick != null) {
            builder.setNegativeButton(nagButtonName, onNegativeButtonClick)
        }

        builder.setCancelable(true)
        val alert = builder.create()
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (isOutSideCancelable) {
            alert.setCanceledOnTouchOutside(true)
        } else {
            alert.setCanceledOnTouchOutside(false)
        }
        alert.show()
        if (changeButtonColor) {
            try {
                alert.setOnShowListener {
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(alert.context, R.color.color_black))
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(alert.context, R.color.colorPrimary))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

private fun updateWindowManagerLayout(dialog: Dialog, isTransparentBackground: Boolean = false) {

    val windowManagerLayoutParams = WindowManager.LayoutParams()
    val window = dialog.window
    if (window != null) {
        windowManagerLayoutParams.copyFrom(window.attributes)
        if (isTransparentBackground)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowManagerLayoutParams.width = SCREEN_WIDTH - SCREEN_WIDTH / 10
        window.attributes = windowManagerLayoutParams
    }
}



fun Activity.showBottomDialogForCustomDate(
    startDate: String, endDate: String,
    listenerCustomDate: (String, String) -> Unit
): BottomSheetDialog {
    val mBottomSheetDialog = BottomSheetDialog(this)
    val sheetViewDialog = layoutInflater.inflate(
        R.layout.dialog_custom_date,
        null
    )
    val edtStartDate =
        sheetViewDialog.findViewById<TextInputEditText>(R.id.edtStartDate)
    val edtEndDate =
        sheetViewDialog.findViewById<TextInputEditText>(R.id.edtEndDate)
    val btnDone =
        sheetViewDialog.findViewById<AppCompatTextView>(R.id.btnDone)
    val ivClose =
        sheetViewDialog.findViewById<AppCompatImageView>(R.id.ivClose)
    val cal = Calendar.getInstance()
    if (startDate.isNotEmpty()) {
        edtStartDate.setText(startDate)
    }
    if (endDate.isNotEmpty()) {
        edtEndDate.setText(endDate)
    }

    val startDateListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "MM-dd-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            edtStartDate.setText(sdf.format(cal.time))

        }
    val endDateListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "MM-dd-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            edtEndDate.setText(sdf.format(cal.time))

        }

    edtStartDate.setOnClickListener {
        DatePickerDialog(
            this, startDateListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    edtEndDate.setOnClickListener {
        DatePickerDialog(
            this, endDateListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    ivClose.setOnClickListener {
        mBottomSheetDialog.dismiss()
    }
    btnDone.setOnClickListener {
        when {
            edtStartDate.text?.trim().toString().isEmpty() -> {
                showToast(getString(R.string.select_start_date), showInReleaseBuild = true)
            }
            edtEndDate.text?.trim().toString().isEmpty() -> {
                showToast(getString(R.string.select_end_date), showInReleaseBuild = true)
            }
            getDateInLong(edtStartDate.text.toString()) >= getDateInLong(edtEndDate.text.toString()) -> {
                showToast(
                    getString(R.string.select_start_date_less_than_end_date),
                    showInReleaseBuild = true
                )
            }
            getDateInLong(edtEndDate.text.toString()) <= getDateInLong(edtStartDate.text.toString()) -> {
                showToast(
                    getString(R.string.select_end_date_less_than_start_date),
                    showInReleaseBuild = true
                )
            }
            else -> {
                listenerCustomDate.invoke(
                    edtStartDate.text.toString(),
                    edtEndDate.text.toString()
                )
                mBottomSheetDialog.dismiss()
            }
        }
    }
    mBottomSheetDialog.setContentView(sheetViewDialog)
    mBottomSheetDialog.show()
    mBottomSheetDialog.setCanceledOnTouchOutside(true)
    return mBottomSheetDialog
}

