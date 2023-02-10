package com.nhlynn.plan_calendar.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nhlynn.plan_calendar.DataViewModel
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.databinding.FragmentDailyCalendarBinding
import com.nhlynn.plan_calendar.delegate.OnDateChangeListener
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnTimeClickListener
import com.nhlynn.plan_calendar.model.EventVO
import java.text.SimpleDateFormat
import java.util.*

class DailyCalendarFragment : Fragment() {
    private var _binding: FragmentDailyCalendarBinding?=null
    private val binding get() = _binding!!

    private val ymdFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val monthFormatter = SimpleDateFormat("MM", Locale.US)
    private val dayFormatter = SimpleDateFormat("dd", Locale.US)

    private lateinit var mDataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataViewModel=ViewModelProvider(this)[DataViewModel::class.java]

        binding.dailyCal.setPreviousIcon(R.drawable.ic_previous)
        binding.dailyCal.setNextIcon(R.drawable.ic_next)
//        binding.dailyCal.setSundayOff(true)
        binding.dailyCal.setWeekendOff(true)
        binding.dailyCal.setLineColor(R.color.black)
        binding.dailyCal.setOneTimeColor(R.color.purple_200)
        binding.dailyCal.setRepeatColor(R.color.red)
        binding.dailyCal.setProcessColor(R.color.white)
        binding.dailyCal.setHolidayEventColor(R.color.blue)
        binding.dailyCal.setPastColor(R.color.black)

        mDataViewModel.getDailyPlanList(monthFormatter.format(Date()),dayFormatter.format(Date()))

        binding.dailyCal.setOnDateChangeListener(object : OnDateChangeListener {
            override fun onDateChange(date: String) {
                mDataViewModel.getDailyPlanList(monthFormatter.format(ymdFormatter.parse(date)!!),
                    dayFormatter.format(ymdFormatter.parse(date)!!))
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

        mDataViewModel.mPlanResponse.observe(viewLifecycleOwner){
            binding.dailyCal.setEvents(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object {
        fun newInstance() = DailyCalendarFragment()
    }
}