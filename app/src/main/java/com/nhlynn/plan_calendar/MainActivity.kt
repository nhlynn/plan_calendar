package com.nhlynn.plan_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.nhlynn.plan_calendar.databinding.ActivityMainBinding
import com.nhlynn.plan_calendar.pager_adapter.CalendarViewPager

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private lateinit var mCalendarViewPager: CalendarViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        val titleList = arrayListOf<String>()
        titleList.add("Daily")
        titleList.add(" Weekly")
        titleList.add("Monthly")

        mCalendarViewPager = CalendarViewPager(this)
        binding.pager.adapter = mCalendarViewPager
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }
}