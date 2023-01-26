package com.nhlynn.plan_calendar.delegate

import com.nhlynn.plan_calendar.model.EventVO

interface OnEventClickListener {
    fun eventClick(event: EventVO)
}