package com.nhlynn.plan_calendar.delegate

interface OnMonthChangeListener {
    fun onMonthChange(currentMonth: String, startDate: String, endDate: String)
}