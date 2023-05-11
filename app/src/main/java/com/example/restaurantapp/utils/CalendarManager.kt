package com.example.restaurantapp.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class CalendarManager {

    companion object {
        const val DATE_FORMAT = "dd MMM yyyy"
    }

    fun openDatePickerDialog(editText: EditText, context: Context) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            context,
            { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, 0, 0)
                calendar.timeInMillis

                editText.setText(
                    getDateTimeFromMillis(
                        calendar.timeInMillis,
                        SimpleDateFormat(
                            DATE_FORMAT,
                            Locale.ENGLISH
                        )
                    )
                )
            },
            startYear,
            startMonth,
            startDay
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun getDateTimeFromMillis(millis: Long, formatter: SimpleDateFormat): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        return formatter.format(calendar.time)
    }

}