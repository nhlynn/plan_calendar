# Plan Calendar

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle maven sbt leiningen Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.nhlynn:plan_calendar:Tag'
	}

Usage
  1.1. Add Daily Plan Calendar to your layout
  
    <com.nhlynn.plan_calendar.DailyCalendar
        android:id="@+id/daily_cal"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="8dp"/>
  
  1.2. You can use layout attributes

    app:holiday_event_color="@color/holiday_event"
    app:line_color="@color/line_color"
    app:next_icon="@drawable/ic_next"
    app:oneTime_color="@color/one_time_event"
    app:past_color="@color/past_event"
    app:previous_icon="@drawable/ic_previous"
    app:progress_color="@color/progress_event"
    app:repeat_color="@color/repeat_event"
    app:sunday_off="true"
    app:weekend_off="true"
 
 
  1.3.Fetch and modify your DailyCalendar in your layout class
  
        val planList= arrayListOf<EventVO>()
        planList.add(EventVO(date = "2023-01-22", startTime = "15:00", endTime = "16:00", eventName = "Event 5",eventType = PlanCalendar.HOLIDAY_EVENT))
        planList.add(EventVO(date = "2023-01-23", startTime = "11:30", endTime = "12:00", eventName = "Event 1",eventType = PlanCalendar.REPEAT_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "13:00", endTime = "18:00", eventName = "Event 2",eventType = PlanCalendar.ONE_TIME_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "12:30", endTime = "13:30", eventName = "Event 3",eventType = PlanCalendar.IN_PROGRESS_EVENT))
        planList.add(EventVO(date = "2023-01-25", startTime = "13:00", endTime = "14:00", eventName = "Event 4",eventType = PlanCalendar.PAST_EVENT))
        planList.add(EventVO(date = "2023-01-26", startTime = "13:00", endTime = "14:00", eventName = "Event 5",eventType = PlanCalendar.ONE_TIME_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "10:30", endTime = "11:30", eventName = "Event 3",eventType = PlanCalendar.IN_PROGRESS_EVENT))

        binding.dailyCal.setEvents(planList)
        binding.dailyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.dailyCal.setNextIcon(R.drawable.ic_next)
        binding.dailyCal.setSundayOff(true)
        binding.dailyCal.setWeekendOff(true)
        binding.dailyCal.setLineColor(R.color.black)
        binding.dailyCal.setOneTimeColor(R.color.purple_200)
        binding.dailyCal.setRepeatColor(R.color.red)
        binding.dailyCal.setProcessColor(R.color.white)
        binding.dailyCal.setHolidayEventColor(R.color.blue)
        binding.dailyCal.setPastColor(R.color.black)

        binding.dailyCal.setOnDateChangeListener(object : OnDateChangeListener {
            override fun onDateChange(date: String) {
                Toast.makeText(requireContext(),date, Toast.LENGTH_LONG).show()
            }
        })

        binding.dailyCal.setOnEventClickListener(object : OnEventClickListener {
            override fun eventClick(event: EventVO) {
                Toast.makeText(requireContext(),event.eventName, Toast.LENGTH_LONG).show()
            }
        })

        binding.dailyCal.setOnTimeClickListener(object : OnTimeClickListener {
            override fun onClick(date: String, time: String) {
                Toast.makeText(requireContext(),"$date --- $time", Toast.LENGTH_LONG).show()
            }

        })
        

  2.1. Add Weekly Plan Calendar to your layout
  
    <com.nhlynn.plan_calendar.WeeklyCalendar
        android:id="@+id/weekly_cal"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="8dp"/>

  2.2. You can use layout attributes
  
    app:holiday_event_color="@color/holiday_event"
    app:header_date_color="@color/blue"
    app:line_color="@color/line_color"
    app:next_icon="@drawable/ic_next"
    app:oneTime_color="@color/one_time_event"
    app:past_color="@color/past_event"
    app:previous_icon="@drawable/ic_previous"
    app:progress_color="@color/progress_event"
    app:repeat_color="@color/repeat_event"
    app:weekend_off="true"
    app:sunday_off="true"

  2.3.Fetch and modify your WeeklyCalendar in your layout class

        val planList= arrayListOf<EventVO>()
        planList.add(EventVO(date = "2023-01-22", startTime = "15:00", endTime = "16:00", eventName = "Event 5",eventType = PlanCalendar.HOLIDAY_EVENT))
        planList.add(EventVO(date = "2023-01-23", startTime = "11:30", endTime = "12:00", eventName = "Event 1",eventType = PlanCalendar.REPEAT_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "13:00", endTime = "18:00", eventName = "Event 2",eventType = PlanCalendar.ONE_TIME_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "12:30", endTime = "13:30", eventName = "Event 3",eventType = PlanCalendar.IN_PROGRESS_EVENT))
        planList.add(EventVO(date = "2023-01-25", startTime = "13:00", endTime = "14:00", eventName = "Event 4",eventType = PlanCalendar.PAST_EVENT))
        planList.add(EventVO(date = "2023-01-26", startTime = "13:00", endTime = "14:00", eventName = "Event 5",eventType = PlanCalendar.ONE_TIME_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "10:30", endTime = "11:30", eventName = "Event 3",eventType = PlanCalendar.IN_PROGRESS_EVENT))

        binding.weeklyCal.setEvents(planList)
        binding.weeklyCal.setHeaderDateColor(R.color.blue)
        binding.weeklyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.weeklyCal.setNextIcon(R.drawable.ic_next)
        binding.weeklyCal.setSundayOff(true)
        binding.weeklyCal.setWeekendOff(true)
        binding.weeklyCal.setLineColor(R.color.black)
        binding.weeklyCal.setOneTimeColor(R.color.purple_200)
        binding.weeklyCal.setRepeatColor(R.color.red)
        binding.weeklyCal.setProcessColor(R.color.white)
        binding.weeklyCal.setHolidayEventColor(R.color.blue)
        binding.weeklyCal.setPastColor(R.color.black)

        binding.weeklyCal.setOnWeekChangeListener(object : OnWeekChangeListener {
            override fun onDateChange(fromDate: String, toDate: String) {
                Toast.makeText(requireContext(),"$fromDate---$toDate", Toast.LENGTH_LONG).show()
            }

        })

        binding.weeklyCal.setOnEventClickListener(object : OnEventClickListener {
            override fun eventClick(event: EventVO) {
                Toast.makeText(requireContext(),event.eventName, Toast.LENGTH_LONG).show()
            }
        })

        binding.weeklyCal.setOnTimeClickListener(object : OnTimeClickListener {
            override fun onClick(date: String, time: String) {
                Toast.makeText(requireContext(),"$date --- $time", Toast.LENGTH_LONG).show()
            }

        })
    
  3.1. Add Monthly Plan Calendar to your layout 
  
    <com.nhlynn.plan_calendar.MonthlyCalendar
        android:id="@+id/monthly_cal"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="8dp"/>

  3.2. You can use layout attributes 
  
    app:holiday_event_color="@color/holiday_event"
    app:header_date_color="@color/blue"
    app:line_color="@color/line_color"
    app:next_icon="@drawable/ic_next"
    app:oneTime_color="@color/one_time_event"
    app:past_color="@color/past_event"
    app:previous_icon="@drawable/ic_previous"
    app:progress_color="@color/progress_event"
    app:repeat_color="@color/repeat_event"
    app:sunday_off="true"
    app:weekend_off="true"

  3.3.Fetch and modify your MonthlyCalendar in your layout class
  
        val planList= arrayListOf<EventVO>()
        planList.add(EventVO(date = "2023-01-22", startTime = "15:00", endTime = "16:00", eventName = "Event 5",eventType = PlanCalendar.HOLIDAY_EVENT))
        planList.add(EventVO(date = "2023-01-23", startTime = "11:30", endTime = "12:00", eventName = "Event 1",eventType = PlanCalendar.REPEAT_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "13:00", endTime = "18:00", eventName = "Event 2",eventType = PlanCalendar.ONE_TIME_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "12:30", endTime = "13:30", eventName = "Event 3",eventType = PlanCalendar.IN_PROGRESS_EVENT))
        planList.add(EventVO(date = "2023-01-25", startTime = "13:00", endTime = "14:00", eventName = "Event 4",eventType = PlanCalendar.PAST_EVENT))
        planList.add(EventVO(date = "2023-01-26", startTime = "13:00", endTime = "14:00", eventName = "Event 5",eventType = PlanCalendar.ONE_TIME_EVENT))
        planList.add(EventVO(date = "2023-01-24", startTime = "10:30", endTime = "11:30", eventName = "Event 3",eventType = PlanCalendar.IN_PROGRESS_EVENT))

        binding.monthlyCal.setEvents(planList)
        binding.monthlyCal.setHeaderDateColor(R.color.blue)
        binding.monthlyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.monthlyCal.setNextIcon(R.drawable.ic_next)
        binding.monthlyCal.setSundayOff(true)
        binding.monthlyCal.setWeekendOff(true)
        binding.monthlyCal.setLineColor(R.color.black)
        binding.monthlyCal.setOneTimeColor(R.color.purple_200)
        binding.monthlyCal.setRepeatColor(R.color.red)
        binding.monthlyCal.setProcessColor(R.color.white)
        binding.monthlyCal.setHolidayEventColor(R.color.blue)
        binding.monthlyCal.setPastColor(R.color.black)

        binding.monthlyCal.setOnDateClickListener(object : OnDateClickListener {
            override fun onClick(date: String) {
                Toast.makeText(requireContext(),date, Toast.LENGTH_LONG).show()
            }

        })

        binding.monthlyCal.setOnEventClickListener(object : OnEventClickListener {
            override fun eventClick(event: EventVO) {
                Toast.makeText(requireContext(),event.eventName, Toast.LENGTH_LONG).show()
            }
        })

        binding.monthlyCal.setOnMonthChangeListener(object : OnMonthChangeListener {
            override fun onMonthChange(date: String) {
                Toast.makeText(requireContext(),date, Toast.LENGTH_LONG).show()
            }

        })
  
  
