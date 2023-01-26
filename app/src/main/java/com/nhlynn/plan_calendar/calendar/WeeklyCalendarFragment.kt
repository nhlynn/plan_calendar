package com.nhlynn.plan_calendar.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.databinding.FragmentWeeklyCalendarBinding
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnTimeClickListener
import com.nhlynn.plan_calendar.delegate.OnWeekChangeListener
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.utils.PlanCalendar

class WeeklyCalendarFragment: Fragment() {
    private var _binding: FragmentWeeklyCalendarBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
//        binding.weeklyCal.setWeekendOff(true)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {
        fun newInstance() = WeeklyCalendarFragment()
    }
}