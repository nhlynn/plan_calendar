package com.nhlynn.plan_calendar.utils

import java.text.SimpleDateFormat
import java.util.*

val defaultCalendar: Calendar = Calendar.getInstance()
var monthMainCalendar: Calendar = Calendar.getInstance()
var ymdFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
val ymFormatter = SimpleDateFormat("yyyy-MM", Locale.US)
var myShowFormatter = SimpleDateFormat("MMM yyyy", Locale.US)
var dateFormat = SimpleDateFormat("dd", Locale.US)
var dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.US)

var allDateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US)
var hmFormat = SimpleDateFormat("HH:mm", Locale.US)
val sdfWeekDay = SimpleDateFormat("dd MMM", Locale.US)

class PlanCalendar{
    companion object{
        const val ONE_TIME_EVENT = 1
        const val REPEAT_EVENT = 2
        const val IN_PROGRESS_EVENT = 3
        const val HOLIDAY_EVENT = 4
        const val PAST_EVENT = 5
    }
}