package com.nhlynn.plan_calendar.adapter.monthly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.adapter.EventAdapter
import com.nhlynn.plan_calendar.databinding.MonthlyCalendarCellBinding
import com.nhlynn.plan_calendar.delegate.OnDateClickListener
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.utils.dateFormat
import com.nhlynn.plan_calendar.utils.dayOfWeekFormat
import com.nhlynn.plan_calendar.utils.ymdFormatter
import java.util.*
import kotlin.collections.ArrayList

class MonthlyDayAdapter(
    private val onDateClickListener: OnDateClickListener,
    private val onEventClickListener: OnEventClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var monthlyDayList = arrayListOf<String>()
    private var dayGroupEventList = mapOf<String, List<EventVO>>()
    private var weekendOff = false
    private var sundayOff = false
    private var lineColor = R.color.line_color
    private var oneTimeColor: Int = R.color.one_time_event
    private var repeatColor: Int = R.color.repeat_event
    private var processColor: Int = R.color.progress_event
    private var holidayColor: Int = R.color.holiday_event
    private var pastColor: Int = R.color.past_event

    class MonthlyViewHolder(val viewBinder: MonthlyCalendarCellBinding) :
        RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            MonthlyCalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as MonthlyViewHolder
        val root = viewHolder.viewBinder

        val date = monthlyDayList[position]

        val showDate = if (date.isNotEmpty()) {
            if (weekendOff &&
                (ymdFormatter.parse(date)?.let { dayOfWeekFormat.format(it) } == "Saturday" ||
                        ymdFormatter.parse(date)?.let { dayOfWeekFormat.format(it) } == "Sunday")
            ) {
                root.tvMonthDate.setTextColor(
                    AppCompatResources.getColorStateList(
                        root.tvMonthDate.context, R.color.red
                    )
                )
            }else if (sundayOff && ymdFormatter.parse(date)
                    ?.let { dayOfWeekFormat.format(it) } == "Sunday"
            ) {
                root.tvMonthDate.setTextColor(
                    AppCompatResources.getColorStateList(
                        root.tvMonthDate.context, R.color.red
                    )
                )
            }else{
                root.tvMonthDate.setTextColor(
                    AppCompatResources.getColorStateList(
                        root.tvMonthDate.context, R.color.black
                    )
                )
            }

            ymdFormatter.parse(date)?.let { dateFormat.format(it) }
        } else {
            ""
        }
        root.tvMonthDate.text = showDate

        root.cvMonthlyCell.setOnClickListener {
            if (date.isNotEmpty()) {
                onDateClickListener.onClick(date)
            }
        }

        root.viewVertical.setBackgroundColor(
            ContextCompat.getColor(
                root.viewVertical.context, lineColor
            )
        )
        root.viewHorizontal.setBackgroundColor(
            ContextCompat.getColor(
                root.viewVertical.context, lineColor
            )
        )

        if (date == ymdFormatter.format(Date())) {
            root.tvMonthDate.setBackgroundResource(R.drawable.circle_drawable)
        } else {
            root.tvMonthDate.background=null
        }

        event(date, root.rvEvent)
    }

    override fun getItemCount(): Int {
        return monthlyDayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(monthlyDayList: ArrayList<String>) {
        this.monthlyDayList = monthlyDayList
        notifyDataSetChanged()
    }


    private fun event(date: String, rvEvent: RecyclerView) {
        eventAdapter(rvEvent, dayGroupEventList[date])
    }

    private fun eventAdapter(rvEvent: RecyclerView, event: List<EventVO>?) {
        if (event.isNullOrEmpty()) {
            rvEvent.visibility = View.GONE
        } else {
            rvEvent.visibility = View.VISIBLE
            val mEventAdapter = EventAdapter(onEventClickListener)
            rvEvent.layoutManager =
                LinearLayoutManager(rvEvent.context, LinearLayoutManager.HORIZONTAL, false)
            rvEvent.adapter = mEventAdapter

            mEventAdapter.setData(event as ArrayList<EventVO>)

            mEventAdapter.setOneTimeColor(oneTimeColor)
            mEventAdapter.setRepeatColor(repeatColor)
            mEventAdapter.setProcessColor(processColor)
            mEventAdapter.setHolidayColor(holidayColor)
            mEventAdapter.setPastColor(pastColor)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(eventList: ArrayList<EventVO>) {
        dayGroupEventList = eventList.groupBy { it.date }
        notifyDataSetChanged()
    }

    fun setLineColor(color: Int) {
        this.lineColor = color
    }

    fun setOneTimeColor(color: Int) {
        this.oneTimeColor = color
    }

    fun setRepeatColor(color: Int) {
        this.repeatColor = color
    }

    fun setProcessColor(color: Int) {
        this.processColor = color
    }

    fun setHolidayColor(color: Int) {
        this.holidayColor = color
    }

    fun setPastColor(color: Int) {
        this.pastColor = color
    }

    fun setSundayOff(status: Boolean) {
        this.sundayOff = status
    }

    fun setWeekendOff(status: Boolean) {
        this.weekendOff = status
    }
}
