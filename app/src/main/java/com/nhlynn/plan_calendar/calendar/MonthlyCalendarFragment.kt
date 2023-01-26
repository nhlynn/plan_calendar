package com.nhlynn.plan_calendar.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.databinding.FragmentMonthlyCalendarBinding
import com.nhlynn.plan_calendar.delegate.OnDateClickListener
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnMonthChangeListener
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.utils.PlanCalendar

class MonthlyCalendarFragment: Fragment() {
    private var _binding: FragmentMonthlyCalendarBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyCalendarBinding.inflate(inflater,container,false)
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

        binding.monthlyCal.setEvents(planList)
        binding.monthlyCal.setHeaderDateColor(R.color.blue)
        binding.monthlyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.monthlyCal.setNextIcon(R.drawable.ic_next)
        binding.monthlyCal.setSundayOff(true)
//        binding.monthlyCal.setWeekendOff(true)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {
        fun newInstance() = MonthlyCalendarFragment()
    }
}