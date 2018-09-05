package com.app.venyoo.helper

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

        fun formatDate(date: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("ru"))
            val formatDate = format.parse(date)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = formatDate.time
            val now = Calendar.getInstance()
            val sameDay = calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
            return when {
                sameDay -> "${calendar[Calendar.HOUR_OF_DAY]} : ${calendar[Calendar.MINUTE]}"
                else -> "${calendar[Calendar.DAY_OF_MONTH]} ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("ru"))}"
            }
        }

        fun formatExactDate(date: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("ru"))
            val formatDate = format.parse(date)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = formatDate.time
            return "${calendar[Calendar.HOUR_OF_DAY]}: ${calendar[Calendar.MINUTE]} ${calendar[Calendar.DAY_OF_MONTH]} ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("ru"))}"
        }

        fun formatTime(time: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return "${calendar[Calendar.DAY_OF_MONTH]} ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("ru"))} ${calendar[Calendar.HOUR_OF_DAY]}: ${calendar[Calendar.MINUTE]}"
        }

    }

}