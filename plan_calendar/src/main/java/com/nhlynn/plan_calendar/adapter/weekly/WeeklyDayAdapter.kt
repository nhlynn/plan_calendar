package com.nhlynn.plan_calendar.adapter.weekly

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.databinding.CalendarCellBinding
import com.nhlynn.plan_calendar.utils.dateFormat
import java.util.Date

class WeeklyDayAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dayList = arrayListOf<String>()
    private var weekendOff = false
    private var sundayOff = false

    class DayViewHolder(val viewBinder: CalendarCellBinding) :
        RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as DayViewHolder
        val root = viewHolder.viewBinder

        root.tvMonthDate.text = dayList[position]

        if (dayList[position] == dateFormat.format(Date())) {
            root.tvMonthDate.setBackgroundResource(R.drawable.circle_drawable)
        } else {
            root.tvMonthDate.background=null
        }

        try {
            if (dayList[position].toInt() > 0) {
                root.tvMonthDate.setTypeface(root.tvMonthDate.typeface, Typeface.NORMAL)
            }
        } catch (e: Exception) {
            root.tvMonthDate.textSize = 14f
            root.tvMonthDate.setTypeface(root.tvMonthDate.typeface, Typeface.BOLD)
        }

        if (weekendOff && (position == 1 || position == 7 || position == 9 || position == 15)) {
            root.tvMonthDate.setTextColor(
                AppCompatResources.getColorStateList(
                    root.tvMonthDate.context,
                    R.color.red
                )
            )
        }

        if (sundayOff && (position == 1 || position == 9)) {
            root.tvMonthDate.setTextColor(
                AppCompatResources.getColorStateList(
                    root.tvMonthDate.context,
                    R.color.red
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dayList: ArrayList<String>) {
        this.dayList = dayList
        notifyDataSetChanged()
    }

    fun setSundayOff(status: Boolean) {
        this.sundayOff = status
    }

    fun setWeekendOff(status: Boolean) {
        this.weekendOff = status
    }
}