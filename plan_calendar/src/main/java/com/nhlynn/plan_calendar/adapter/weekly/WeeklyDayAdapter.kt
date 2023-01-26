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
import com.nhlynn.plan_calendar.utils.ymdFormatter
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

        val date=dayList[position]

        root.tvMonthDate.text = try {
            ymdFormatter.parse(date)?.let { dateFormat.format(it) }
        }catch (e: Exception){
            date
        }

        if (dayList[position] == ymdFormatter.format(Date())) {
            root.tvMonthDate.setBackgroundResource(R.drawable.circle_drawable)
        } else {
            root.tvMonthDate.background=null
        }

        if (position > 8) {
            root.tvMonthDate.setTypeface(root.tvMonthDate.typeface, Typeface.NORMAL)
        }else{
            root.tvMonthDate.textSize = 14f
            root.tvMonthDate.setTypeface(root.tvMonthDate.typeface, Typeface.BOLD)
        }

        val color=if (weekendOff && (position == 1 || position == 7 || position == 9 || position == 15)) {
            R.color.red
        }else if (sundayOff && (position == 1 || position == 9)) {
            R.color.red
        }else {
            R.color.black
        }
        root.tvMonthDate.setTextColor(
            AppCompatResources.getColorStateList(
                root.tvMonthDate.context,
                color
            )
        )
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