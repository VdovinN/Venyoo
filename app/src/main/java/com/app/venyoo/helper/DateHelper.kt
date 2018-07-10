package com.app.venyoo.helper

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

        fun formatDate(date: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
            val formatDate = format.parse(date)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = formatDate.time
            val now = Calendar.getInstance()
            val sameDay = calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
            return when {
                sameDay -> "" + calendar[Calendar.MINUTE] + " : " + calendar[Calendar.SECOND]
                else -> "" + calendar[Calendar.DAY_OF_MONTH] + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
            }
        }

    }

}