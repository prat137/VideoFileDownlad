package com.example.demoapp.ui.taskone

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.demoapp.R
import com.example.demoapp.ui.home.MainActivity
import com.example.demoapp.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_task_one.*
import java.text.SimpleDateFormat
import java.util.*


class TaskOneFragment : Fragment(), View.OnClickListener {
    private lateinit var context: MainActivity
    private lateinit var timePicker1: TimePickerDialog
    private lateinit var timePicker2: TimePickerDialog
    private lateinit var lstData: MutableList<String>
    private var date1: Date? = null
    private var date2: Date? = null
    private lateinit var dateFormat: SimpleDateFormat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        addClickListener()
        timePickerDialog()
    }

    private fun init() {
        lstData = ArrayList()

    }

    private fun timePickerDialog() {
        dateFormat = SimpleDateFormat("hh:mm aa")
        timePicker1 = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                btnTime1.text = updateTime(hourOfDay, minute)

                date1 = dateFormat.parse(updateTime(hourOfDay, minute))
            }, 0, 0, false
        )
        timePicker2 = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                btnTime2.text = updateTime(hourOfDay, minute)
                date2 = dateFormat.parse(updateTime(hourOfDay, minute))
            }, 0, 0, false
        )
    }


    private fun addClickListener() {
        btnTime1.setOnClickListener(this)
        btnTime2.setOnClickListener(this)
        btnGo.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnTime1 -> {
                timePicker1.show()
            }
            R.id.btnTime2 -> {
                timePicker2.show()
            }
            R.id.btnGo -> {
                if (date1 != null && date2 != null) {
                    calculateTime()
                } else {
                    context.showToast("Please select both time", Toast.LENGTH_LONG, false)
                }

            }
        }

    }

    private fun calculateTime() {
        lstData.clear()
        if (date1!!.before(date2)) {
            while (!date1!!.after(date2)) {
                Log.e("abc", "" + dateFormat.format(date1))
                lstData.add(dateFormat.format(date1))
                val cal = Calendar.getInstance() // creates calendar
                cal.time = date1 // sets calendar time/date
                cal.add(Calendar.MINUTE, 1) // adds one hour
                date1 = cal.time
                Log.e("abc", "")
            }
        } else {
            while (!date2!!.after(date1)) {
                Log.e("abc", "" + dateFormat.format(date2))
                lstData.add(dateFormat.format(date2))
                val cal = Calendar.getInstance() // creates calendar
                cal.time = date2 // sets calendar time/date
                cal.add(Calendar.MINUTE, 1) // adds one hour
                date2 = cal.time
                Log.e("abc", "")
            }
        }
        date1 = null
        date2 = null
        btnTime1.text = ""
        btnTime2.text = ""
        val sb = StringBuilder()
        lstData.forEach {
            Log.e("finalData", it)
            sb.append(it)
        }

        Log.e("value with am pm", sb.toString())
        Log.e("value without ampm", sb.toString().filter { it.isDigit() })
        Log.e("value distinct", sb.toString().filter { it.isDigit() })

        val digitString = sb.toString().filter { it.isDigit() }

        val chars: CharArray = getCharArray(toCharacterArray(digitString))

        chars.forEach {
            Log.e("value distinct add", "" + it)
        }
    }


    private fun getCharArray(array: CharArray): CharArray {
        var _array = ""
        for (i in array.indices) {
            if (_array.indexOf(array[i]) == -1) // check if a char already exist, if not exist then return -1
                _array += array[i] // add new char
        }
        return _array.toCharArray()
    }

    private fun toCharacterArray(str: String): CharArray {
        return str.toCharArray()
    }

    private fun updateTime(hours: Int, mins: Int): String {
        var hours = hours
        var timeSet = ""
        if (hours > 12) {
            hours -= 12
            timeSet = "PM"
        } else if (hours == 0) {
            hours += 12
            timeSet = "AM"
        } else if (hours == 12) timeSet = "PM" else timeSet = "AM"
        var minutes = ""
        minutes = if (mins < 10) "0$mins" else mins.toString()
        val time = StringBuilder().append(hours).append(':')
            .append(minutes).append(" ").append(timeSet).toString()
        return time
    }


}