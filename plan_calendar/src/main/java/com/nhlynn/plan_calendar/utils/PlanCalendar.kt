package com.nhlynn.plan_calendar.utils

import java.text.SimpleDateFormat
import java.util.*

val defaultCalendar: Calendar = Calendar.getInstance()
val monthMainCalendar: Calendar = Calendar.getInstance()
val ymdFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
val ymFormatter = SimpleDateFormat("yyyy-MM", Locale.US)
val myShowFormatter = SimpleDateFormat("MMM yyyy", Locale.US)
val dateFormat = SimpleDateFormat("dd", Locale.US)
val monthFormatter = SimpleDateFormat("MM", Locale.US)
val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.US)

val allDateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US)
val hmFormat = SimpleDateFormat("HH:mm", Locale.US)
val sdfWeekDay = SimpleDateFormat("dd MMM", Locale.US)

class PlanCalendar {
    companion object {
        const val ONE_TIME_EVENT = 1
        const val REPEAT_EVENT = 2
        const val IN_PROGRESS_EVENT = 3
        const val HOLIDAY_EVENT = 4
        const val PAST_EVENT = 5
    }
}