package com.nhlynn.plan_calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.nhlynn.plan_calendar.adapter.daily.HourAdapter
import com.nhlynn.plan_calendar.delegate.*
import com.nhlynn.plan_calendar.model.*
import com.nhlynn.plan_calendar.utils.*
import java.util.*
import kotlin.collections.ArrayList

class DailyCalendar constructor(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs), OnTimeClickListener, OnEventClickListener {

    companion object {
        private var previousIcon: Int? = null
        private var nextIcon: Int? = null
        private var weekendOff: Boolean = false
        private var sundayOff: Boolean = false
        private var lineColor: Int = R.color.line_color
        private var oneTimeColor: Int = R.color.one_time_event
        private var repeatColor: Int = R.color.repeat_event
        private var processColor: Int = R.color.progress_event
        private var holidayColor: Int = R.color.holiday_event
        private var pastColor: Int = R.color.past_event
    }

    private var mainCalendar: Calendar = Calendar.getInstance()
    private var mAdapter: HourAdapter

    private var tvDate: MaterialTextView
    private var btnPrevious: AppCompatImageButton
    private var btnNext: AppCompatImageButton
    private var rvDate: RecyclerView

    private var onDateChangeListener: OnDateChangeListener? = null
    private var onEventClickListener: OnEventClickListener? = null
    private var onTimeClickListener: OnTimeClickListener? = null

    init {
        val myAttribute =
            context.theme.obtainStyledAttributes(attrs, R.styleable.LynnCustomizeCalendar, 0, 0)

        previousIcon =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_previous_icon,
                R.drawable.ic_previous
            )
        nextIcon =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_next_icon,
                R.drawable.ic_next
            )
        lineColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_line_color,
                R.color.line_color
            )
        weekendOff = myAttribute.getBoolean(R.styleable.LynnCustomizeCalendar_weekend_off, false)
        sundayOff = myAttribute.getBoolean(R.styleable.LynnCustomizeCalendar_sunday_off, false)
        oneTimeColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_oneTime_color,
                R.color.line_color
            )
        repeatColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_repeat_color,
                R.color.line_color
            )
        processColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_progress_color,
                R.color.line_color
            )
        holidayColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_holiday_event_color,
                R.color.line_color
            )
        pastColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_past_color,
                R.color.line_color
            )

        myAttribute.recycle()

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView: View = inflater.inflate(R.layout.daily_calendar, this, true)

        tvDate = rootView.findViewById(R.id.tv_header_date)
        btnPrevious = rootView.findViewById(R.id.btn_previous)
        btnNext = rootView.findViewById(R.id.btn_next)
        rvDate = rootView.findViewById(R.id.rv_date)

        mAdapter = HourAdapter(this, this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvDate.layoutManager = layoutManager
        rvDate.adapter = mAdapter

        mAdapter.setLineColor(lineColor)

        setDayHour()

        nextIcon?.let { btnNext.setImageResource(it) }

        previousIcon?.let { btnPrevious.setImageResource(it) }

        btnNext.setOnClickListener {
            onNext()
        }

        btnPrevious.setOnClickListener {
            onPrevious()
        }
    }

    private fun satSunOff() {
        if (weekendOff) {
            val color = if (dayOfWeekFormat.format(mainCalendar.time) == "Saturday" ||
                dayOfWeekFormat.format(mainCalendar.time) == "Sunday"
            ) {
                R.color.red
            } else {
                R.color.black
            }
            setDateColor(color)
        }
        if (sundayOff) {
            val color = if (dayOfWeekFormat.format(mainCalendar.time) == "Sunday") {
                R.color.red
            } else {
                R.color.black
            }
            setDateColor(color)
        }
    }

    private fun setDateColor(color: Int) {
        tvDate.setTextColor(
            AppCompatResources.getColorStateList(
                tvDate.context,
                color
            )
        )
    }

    private fun onNext() {
        mainCalendar.set(
            Calendar.DAY_OF_MONTH,
            mainCalendar.get(Calendar.DAY_OF_MONTH) + 1
        )
        setDayHour()
    }

    private fun onPrevious() {
        mainCalendar.set(
            Calendar.DAY_OF_MONTH,
            mainCalendar.get(Calendar.DAY_OF_MONTH) - 1
        )
        setDayHour()
    }

    private fun setDayHour() {
        tvDate.text = allDateFormat.format(mainCalendar.time)
        satSunOff()

        val hourList = arrayListOf<HourVO>()
        for (value in 0..23) {
            if (value < 10) {
                hourList.add(
                    HourVO(
                        hour = "0$value",
                        date = ymdFormatter.format(mainCalendar.time)
                    )
                )
            } else {
                hourList.add(HourVO(hour = "$value", date = ymdFormatter.format(mainCalendar.time)))
            }
        }

        mAdapter.setData(hourList)
        mAdapter.clearEvent()

        if (onDateChangeListener != null) {
            onDateChangeListener!!.onDateChange(
                ymdFormatter.format(
                    mainCalendar.time
                )
            )
        }
    }

    fun setOnDateChangeListener(onDateChangeListener: OnDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener
    }

    fun setOnEventClickListener(onEventClickListener: OnEventClickListener) {
        this.onEventClickListener = onEventClickListener
    }

    fun setOnTimeClickListener(onTimeClickListener: OnTimeClickListener) {
        this.onTimeClickListener = onTimeClickListener
    }

    override fun onClick(date: String, time: String) {
        if (onTimeClickListener != null) {
            onTimeClickListener!!.onClick(date, time)
        }
    }

    override fun eventClick(event: EventVO) {
        if (onEventClickListener != null) {
            onEventClickListener!!.eventClick(event)
        }
    }

    //User Change by Programmatically
    fun setEvents(eventList: ArrayList<EventVO>) {
        mAdapter.setEvent(eventList)
    }

    fun getStartDate():String{
        return ymdFormatter.format(
            mainCalendar.time
        )
    }

    fun getEndDate():String{
        return ymdFormatter.format(
            mainCalendar.time
        )
    }

    fun clearEvents() {
        mAdapter.clearEvent()
    }

    fun setPreviousIcon(icon: Int) {
        btnPrevious.setImageResource(icon)
    }

    fun setNextIcon(icon: Int) {
        btnNext.setImageResource(icon)
    }

    fun setWeekendOff(status: Boolean) {
        weekendOff = status
        setDayHour()
    }

    fun setSundayOff(status: Boolean) {
        sundayOff = status
        setDayHour()
    }

    fun setLineColor(color: Int) {
        mAdapter.setLineColor(color)
    }

    fun setOneTimeColor(color: Int) {
        mAdapter.setOneTimeColor(color)
    }

    fun setRepeatColor(color: Int) {
        mAdapter.setRepeatColor(color)
    }

    fun setProcessColor(color: Int) {
        mAdapter.setProcessColor(color)
    }

    fun setHolidayEventColor(color: Int) {
        mAdapter.setHolidayEventColor(color)
    }

    fun setPastColor(color: Int) {
        mAdapter.setPastColor(color)
    }
}