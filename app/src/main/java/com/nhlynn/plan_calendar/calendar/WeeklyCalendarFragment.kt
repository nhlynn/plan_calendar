package com.nhlynn.plan_calendar.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nhlynn.plan_calendar.DataViewModel
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.databinding.FragmentWeeklyCalendarBinding
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnTimeClickListener
import com.nhlynn.plan_calendar.delegate.OnWeekChangeListener
import com.nhlynn.plan_calendar.model.EventVO
import java.text.SimpleDateFormat
import java.util.*

class WeeklyCalendarFragment: Fragment() {
    private var _binding: FragmentWeeklyCalendarBinding?=null
    private val binding get() = _binding!!

    private val ymdFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val monthFormatter = SimpleDateFormat("MM", Locale.US)
    private val dayFormatter = SimpleDateFormat("dd", Locale.US)

    private lateinit var mDataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataViewModel= ViewModelProvider(this)[DataViewModel::class.java]

        binding.weeklyCal.setHeaderDateColor(R.color.blue)
        binding.weeklyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.weeklyCal.setNextIcon(R.drawable.ic_next)
        binding.weeklyCal.setSundayOff(true)
//        binding.weeklyCal.setWeekendOff(true)
        binding.weeklyCal.setLineColor(R.color.black)
//        binding.weeklyCal.setOneTimeColor(R.color.purple_200)
//        binding.weeklyCal.setRepeatColor(R.color.red)
//        binding.weeklyCal.setProcessColor(R.color.white)
//        binding.weeklyCal.setHolidayEventColor(R.color.blue)
//        binding.weeklyCal.setPastColor(R.color.black)

        Log.d("LogData","Weekly Start Date = ${binding.weeklyCal.getStartDate()}")
        Log.d("LogData","Weekly End Date = ${binding.weeklyCal.getEndDate()}")
        mDataViewModel.getWeeklyPlanList(monthFormatter.format(ymdFormatter.parse(binding.weeklyCal.getStartDate())!!),
            dayFormatter.format(ymdFormatter.parse(binding.weeklyCal.getEndDate())!!),
            dayFormatter.format(ymdFormatter.parse(binding.weeklyCal.getStartDate())!!))

        binding.weeklyCal.setOnWeekChangeListener(object : OnWeekChangeListener {
            override fun onDateChange(fromDate: String, toDate: String) {
                Log.d("LogData","Weekly Start Date1 = ${binding.weeklyCal.getStartDate()}")
                Log.d("LogData","Weekly End Date1 = ${binding.weeklyCal.getEndDate()}")

                Log.d("LogData","Weekly Start Date Listener = $fromDate")
                Log.d("LogData","Weekly End Date Listener = $toDate")

                mDataViewModel.getWeeklyPlanList(monthFormatter.format(ymdFormatter.parse(fromDate)!!),
                    dayFormatter.format(ymdFormatter.parse(fromDate)!!),
                    dayFormatter.format(ymdFormatter.parse(toDate)!!))
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

        mDataViewModel.mPlanResponse.observe(viewLifecycleOwner){
            binding.weeklyCal.setEvents(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {
        fun newInstance() = WeeklyCalendarFragment()
    }
}