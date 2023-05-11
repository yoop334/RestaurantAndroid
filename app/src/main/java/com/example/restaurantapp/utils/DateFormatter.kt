package com.example.restaurantapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private const val DATE_TIME_FORMAT = "dd MMM yyyy HH:mm"

    private val simpleDateFormat = SimpleDateFormat(
        DATE_TIME_FORMAT,
        Locale.ENGLISH
    )

    fun getDateTimeFromMillis(
        millis: Long,
        formatter: SimpleDateFormat = simpleDateFormat
    ): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        return formatter.format(calendar.time)
    }

}