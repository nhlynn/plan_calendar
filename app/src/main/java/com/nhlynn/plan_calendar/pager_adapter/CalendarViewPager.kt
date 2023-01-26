package com.nhlynn.plan_calendar.pager_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nhlynn.plan_calendar.calendar.DailyCalendarFragment
import com.nhlynn.plan_calendar.calendar.MonthlyCalendarFragment
import com.nhlynn.plan_calendar.calendar.WeeklyCalendarFragment

class CalendarViewPager(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                DailyCalendarFragment.newInstance()
            }
            1->{
                WeeklyCalendarFragment.newInstance()
            }
            else -> MonthlyCalendarFragment.newInstance()
        }
    }
}