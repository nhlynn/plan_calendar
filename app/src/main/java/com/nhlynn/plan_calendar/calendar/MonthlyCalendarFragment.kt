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
import com.nhlynn.plan_calendar.databinding.FragmentMonthlyCalendarBinding
import com.nhlynn.plan_calendar.delegate.OnDateClickListener
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnMonthChangeListener
import com.nhlynn.plan_calendar.model.EventVO
import java.text.SimpleDateFormat
import java.util.*

class MonthlyCalendarFragment: Fragment() {
    private var _binding: FragmentMonthlyCalendarBinding?=null
    private val binding get() = _binding!!

    private val ymdFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val monthFormatter = SimpleDateFormat("MM", Locale.US)

    private lateinit var mDataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataViewModel= ViewModelProvider(this)[DataViewModel::class.java]

        binding.monthlyCal.setHeaderDateColor(R.color.blue)
        binding.monthlyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.monthlyCal.setNextIcon(R.drawable.ic_next)
        binding.monthlyCal.setSundayOff(true)
//        binding.monthlyCal.setWeekendOff(true)
        binding.monthlyCal.setLineColor(R.color.black)
//        binding.monthlyCal.setOneTimeColor(R.color.purple_200)
//        binding.monthlyCal.setRepeatColor(R.color.red)
//        binding.monthlyCal.setProcessColor(R.color.white)
//        binding.monthlyCal.setHolidayEventColor(R.color.blue)
//        binding.monthlyCal.setPastColor(R.color.black)

        mDataViewModel.getMonthlyPlanList(monthFormatter.format(Date()))

        Log.d("LogData","Monthly Start Date = ${binding.monthlyCal.getStartDate()}")
        Log.d("LogData","Monthly End Date = ${binding.monthlyCal.getEndDate()}")

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
            override fun onMonthChange(currentMonth: String,startDate:String, endDate:String) {
                Log.d("LogData","Monthly Start Date = ${binding.monthlyCal.getStartDate()}")
                Log.d("LogData","Monthly End Date = ${binding.monthlyCal.getEndDate()}")

                Log.d("LogData","Monthly Date Listener = $currentMonth")
                Log.d("LogData","Monthly Start Date Listener = $startDate")
                Log.d("LogData","Monthly End Date Listener = $endDate")
                mDataViewModel.getMonthlyPlanList(currentMonth)
            }

        })

        mDataViewModel.mPlanResponse.observe(viewLifecycleOwner){
            binding.monthlyCal.setEvents(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {
        fun newInstance() = MonthlyCalendarFragment()
    }
}