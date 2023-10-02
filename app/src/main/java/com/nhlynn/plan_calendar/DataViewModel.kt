package com.nhlynn.plan_calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.utils.PlanCalendar

class DataViewModel : ViewModel() {
    var mPlanResponse = MutableLiveData<ArrayList<EventVO>>()
    private val hourList = arrayListOf(
        "01:00,02:00", "02:00,03:00", "03:00,04:00", "04:00,05:00",
        "05:00,06:00", "06:00,07:00", "07:00,08:00", "08:00,09:00", "09:00,10:00", "10:00,11:00",
        "11:00,12:00", "12:00,13:00", "13:00,14:00", "15:00,16:00", "16:00,17:00", "17:00,18:00",
        "18:00,19:00", "19:00,20:00", "20:00,21:00", "21:00,22:00", "22:00,23:00", "23:00,00:00"
    )

    private fun getRandomTime():String{
        return hourList[(0..21).random()]
    }

    fun getMonthlyPlanList(month: String) {
        val monthlyPlanList = arrayListOf(
            EventVO(
                eventId = 1,
                date = "2023-$month-02",
                startTime = "01:00",
                endTime = "01:30",
                eventName = "Event 1",
                eventType = PlanCalendar.HOLIDAY_EVENT
            ),
            EventVO(
                eventId = 2,
                date = "2023-$month-10",
                startTime = "03:30",
                endTime = "04:30",
                eventName = "Event 2",
                eventType = PlanCalendar.ONE_TIME_EVENT
            ),
            EventVO(
                eventId = 3,
                date = "2023-$month-20",
                startTime = "11:00",
                endTime = "12:30",
                eventName = "Event 3",
                eventType = PlanCalendar.IN_PROGRESS_EVENT
            ),
            EventVO(
                eventId = 4,
                date = "2023-$month-22",
                startTime = "14:00",
                endTime = "15:00",
                eventName = "Event 4",
                eventType = PlanCalendar.PAST_EVENT
            ),
            EventVO(
                eventId = 5,
                date = "2023-$month-22",
                startTime = "15:00",
                endTime = "15:30",
                eventName = "Event 5",
                eventType = PlanCalendar.ONE_TIME_EVENT
            ),
            EventVO(
                eventId = 6,
                date = "2023-$month-27",
                startTime = "16:00",
                endTime = "17:30",
                eventName = "Event 6",
                eventType = PlanCalendar.HOLIDAY_EVENT
            )
        )
        mPlanResponse.postValue(monthlyPlanList)
    }

    fun getDailyPlanList(month: String, day: String) {
        val monthlyPlanList = arrayListOf(
            EventVO(
                eventId = 1,
                date = "2023-$month-$day",
                startTime = "01:00",
                endTime = "01:30",
                eventName = "Event 1",
                eventType = PlanCalendar.HOLIDAY_EVENT
            ),
            EventVO(
                eventId = 2,
                date = "2023-$month-$day",
                startTime = "03:30",
                endTime = "04:30",
                eventName = "Event 2",
                eventType = PlanCalendar.ONE_TIME_EVENT
            ),
            EventVO(
                eventId = 3,
                date = "2023-$month-$day",
                startTime = "11:00",
                endTime = "12:30",
                eventName = "Event 3",
                eventType = PlanCalendar.IN_PROGRESS_EVENT
            ),
            EventVO(
                eventId = 4,
                date = "2023-$month-$day",
                startTime = "14:00",
                endTime = "15:00",
                eventName = "Event 4",
                eventType = PlanCalendar.PAST_EVENT
            ),
            EventVO(
                eventId = 5,
                date = "2023-$month-$day",
                startTime = "15:00",
                endTime = "15:30",
                eventName = "Event 5",
                eventType = PlanCalendar.ONE_TIME_EVENT
            ),
            EventVO(
                eventId = 6,
                date = "2023-$month-$day",
                startTime = "16:00",
                endTime = "17:30",
                eventName = "Event 6",
                eventType = PlanCalendar.HOLIDAY_EVENT
            )
        )
        mPlanResponse.postValue(monthlyPlanList)
    }

    fun getWeeklyPlanList(month: String, startDay: String, endDay:String) {
        val planList = arrayListOf<EventVO>()
        var eventCount = 0
        for (day in startDay.toInt()..endDay.toInt()){
            val time=getRandomTime()
            eventCount+=1
            val d=if (day>9) day else "0$day"
            val event=EventVO(
                eventId = 1,
                date = "2023-$month-$d",
                startTime = time.split(",")[0],
                endTime = time.split(",")[1],
                eventName = "Event $eventCount",
                eventType = PlanCalendar.HOLIDAY_EVENT
            )
            planList.add(event)
        }
        mPlanResponse.postValue(planList)
    }
}