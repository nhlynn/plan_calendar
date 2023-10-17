package com.nhlynn.plan_calendar.model

data class EventVO(
    val eventId: Any? = null,
    val date: String,
    val startTime: String,
    val endTime: String,
    val eventName: String,
    val eventType: Int? = null
)