package com.nhlynn.plan_calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.nhlynn.plan_calendar.adapter.monthly.*
import com.nhlynn.plan_calendar.delegate.*
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.utils.*
import java.util.*

class MonthlyCalendar constructor(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs), OnDateClickListener, OnEventClickListener {

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
        private var headerColor: Int = R.color.blue

        private var NEXT = "PLUS"
        private var PREVIOUS = "MINUS"
    }

    private var mMonthlyDayAdapter: MonthlyDayAdapter

    private var tvDate: MaterialTextView
    private var tvSat: MaterialTextView
    private var tvSun: MaterialTextView
    private var btnPrevious: AppCompatImageButton
    private var btnNext: AppCompatImageButton
    private var rvDate: RecyclerView

    private var onMonthChangeListener: OnMonthChangeListener? = null
    private var onEventClickListener: OnEventClickListener? = null
    private var onDateClickListener: OnDateClickListener? = null

    init {
        val myAttribute =
            context.theme.obtainStyledAttributes(attrs, R.styleable.LynnCustomizeCalendar, 0, 0)

        headerColor =
            myAttribute.getResourceId(
                R.styleable.LynnCustomizeCalendar_header_date_color,
                R.color.blue
            )

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
        val rootView: View = inflater.inflate(R.layout.monthly_calendar, this, true)

        tvDate = rootView.findViewById(R.id.tv_header_date)
        tvSat = rootView.findViewById(R.id.lbl_sat)
        tvSun = rootView.findViewById(R.id.lbl_sun)
        btnPrevious = rootView.findViewById(R.id.btn_previous)
        btnNext = rootView.findViewById(R.id.btn_next)
        rvDate = rootView.findViewById(R.id.rv_date)

        getMonthDay()
        setHeaderDateColor(headerColor)

        mMonthlyDayAdapter = MonthlyDayAdapter(this, this)
        rvDate.layoutManager = GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false)
        rvDate.adapter = mMonthlyDayAdapter
        mMonthlyDayAdapter.setData(daysInMonthArray())

        nextIcon?.let { btnNext.setImageResource(it) }

        previousIcon?.let { btnPrevious.setImageResource(it) }

        btnNext.setOnClickListener {
            calculateDay(NEXT)
        }

        btnPrevious.setOnClickListener {
            calculateDay(PREVIOUS)
        }

        setLineColor(lineColor)
        setSundayOff(sundayOff)
        setWeekendOff(weekendOff)
    }

    private fun calculateDay(sign: String) {
        if (sign == PREVIOUS) {
            monthMainCalendar.set(
                Calendar.MONTH,
                monthMainCalendar.get(Calendar.MONTH) - 1
            )

        } else {
            monthMainCalendar.set(
                Calendar.MONTH,
                monthMainCalendar.get(Calendar.MONTH) + 1
            )
        }

        getMonthDay()
        mMonthlyDayAdapter.setData(daysInMonthArray())
    }

    private fun getMonthDay() {
        tvDate.text = myShowFormatter.format(monthMainCalendar.time)
        if (onMonthChangeListener != null) {
            onMonthChangeListener!!.onMonthChange(
                monthFormatter.format(
                    monthMainCalendar.time
                ),
                getStartDate(),
                getEndDate()
            )
        }
    }

    private fun daysInMonthArray(): ArrayList<String> {
        val dateModelList: ArrayList<String> = ArrayList()
        // set date start of month
        val calendar = Calendar.getInstance()
        calendar.time = monthMainCalendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val monthBeginningCell: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)
        dateModelList.clear()

        while (dateModelList.size < 42) {
            dateModelList.add(ymdFormatter.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dateModelList
    }

    override fun eventClick(event: EventVO) {
        if (onEventClickListener != null) {
            onEventClickListener!!.eventClick(event)
        }
    }

    override fun onClick(date: String) {
        if (onDateClickListener != null) {
            onDateClickListener!!.onClick(date)
        }
    }


    //User Use Functions
    fun setEvents(eventList: ArrayList<EventVO>) {
        mMonthlyDayAdapter.setEvent(eventList)
    }

    fun getStartDate(): String {
        val calendar = Calendar.getInstance()
        calendar.time = monthMainCalendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return ymdFormatter.format(
            calendar.time
        )
    }

    fun getEndDate(): String {
        val monthLastDate: Int = monthMainCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val calendar = Calendar.getInstance()
        calendar.time = monthMainCalendar.time
        calendar.set(Calendar.DAY_OF_MONTH, monthLastDate)
        return ymdFormatter.format(
            calendar.time
        )
    }

    fun setOnEventClickListener(onEventClickListener: OnEventClickListener) {
        this.onEventClickListener = onEventClickListener
    }

    fun setOnDateClickListener(onDateClickListener: OnDateClickListener) {
        this.onDateClickListener = onDateClickListener
    }

    fun setOnMonthChangeListener(monthChangeListener: OnMonthChangeListener) {
        onMonthChangeListener = monthChangeListener
    }

    fun setHeaderDateColor(color: Int) {
        tvDate.setTextColor(
            AppCompatResources.getColorStateList(
                tvDate.context,
                color
            )
        )
    }

    fun setPreviousIcon(icon: Int) {
        btnPrevious.setImageResource(icon)
    }

    fun setNextIcon(icon: Int) {
        btnNext.setImageResource(icon)
    }

    fun setWeekendOff(status: Boolean) {
        weekendOff = status
        if (weekendOff) {
            tvSat.setTextColor(ContextCompat.getColor(tvSun.context, R.color.red))
            tvSun.setTextColor(ContextCompat.getColor(tvSun.context, R.color.red))
        }
        mMonthlyDayAdapter.setWeekendOff(status)
    }

    fun setSundayOff(status: Boolean) {
        sundayOff = status
        if (sundayOff) {
            tvSun.setTextColor(ContextCompat.getColor(tvSun.context, R.color.red))
        }
        mMonthlyDayAdapter.setSundayOff(status)
    }

    fun setLineColor(color: Int) {
        mMonthlyDayAdapter.setLineColor(color)
    }

    fun setOneTimeColor(color: Int) {
        mMonthlyDayAdapter.setOneTimeColor(color)
    }

    fun setRepeatColor(color: Int) {
        mMonthlyDayAdapter.setRepeatColor(color)
    }

    fun setProcessColor(color: Int) {
        mMonthlyDayAdapter.setProcessColor(color)
    }

    fun setHolidayEventColor(color: Int) {
        mMonthlyDayAdapter.setHolidayColor(color)
    }

    fun setPastColor(color: Int) {
        mMonthlyDayAdapter.setPastColor(color)
    }
}