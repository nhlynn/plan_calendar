package com.nhlynn.plan_calendar

import android.annotation.SuppressLint
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
import com.nhlynn.plan_calendar.adapter.weekly.*
import com.nhlynn.plan_calendar.delegate.*
import com.nhlynn.plan_calendar.model.*
import com.nhlynn.plan_calendar.utils.*
import java.util.*
import kotlin.collections.ArrayList

class WeeklyCalendar constructor(
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
        private var headerColor: Int = R.color.blue

        private var NEXT = "PLUS"
        private var PREVIOUS = "MINUS"
    }

    private var mainCalendar: Calendar = Calendar.getInstance()
    private var hourCalendar: Calendar = Calendar.getInstance()
    private var mWeeklyHourAdapter: WeeklyHourAdapter
    private var weekStartDate:String=""
    private var weekEndDate:String=""

    private var tvDate: MaterialTextView
    private var lblSun: MaterialTextView
    private var lblSat: MaterialTextView
    private var tvSun: MaterialTextView
    private var tvMon: MaterialTextView
    private var tvTue: MaterialTextView
    private var tvWed: MaterialTextView
    private var tvThu: MaterialTextView
    private var tvFri: MaterialTextView
    private var tvSat: MaterialTextView
    private var btnPrevious: AppCompatImageButton
    private var btnNext: AppCompatImageButton
    private var rvHour: RecyclerView

    private var onWeekChangeListener: OnWeekChangeListener? = null
    private var onEventClickListener: OnEventClickListener? = null
    private var onTimeClickListener: OnTimeClickListener? = null

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
        val rootView: View = inflater.inflate(R.layout.weekly_calendar, this, true)

        tvDate = rootView.findViewById(R.id.tv_header_date)
        lblSun = rootView.findViewById(R.id.lbl_sun)
        lblSat = rootView.findViewById(R.id.lbl_sat)
        tvSun = rootView.findViewById(R.id.tv_sun)
        tvMon = rootView.findViewById(R.id.tv_mon)
        tvTue = rootView.findViewById(R.id.tv_tue)
        tvWed = rootView.findViewById(R.id.tv_wed)
        tvThu = rootView.findViewById(R.id.tv_thu)
        tvFri = rootView.findViewById(R.id.tv_fri)
        tvSat = rootView.findViewById(R.id.tv_sat)
        btnPrevious = rootView.findViewById(R.id.btn_previous)
        btnNext = rootView.findViewById(R.id.btn_next)
        rvHour = rootView.findViewById(R.id.rv_hour)

        setHeaderDateColor(headerColor)

        mWeeklyHourAdapter = WeeklyHourAdapter(this, this)
        rvHour.layoutManager = GridLayoutManager(context, 8, LinearLayoutManager.VERTICAL, false)
        rvHour.adapter = mWeeklyHourAdapter
        mWeeklyHourAdapter.setData(getDayHour())

        nextIcon?.let { btnNext.setImageResource(it) }

        previousIcon?.let { btnPrevious.setImageResource(it) }

        btnNext.setOnClickListener {
            calculateDay(NEXT)
        }

        btnPrevious.setOnClickListener {
            calculateDay(PREVIOUS)
        }

        getWeekDay()
        setLineColor(lineColor)
        setSundayOff(sundayOff)
        setWeekendOff(weekendOff)
    }

    private fun calculateDay(sign: String) {
        if (sign == PREVIOUS) {
            mainCalendar.set(
                Calendar.DAY_OF_MONTH,
                mainCalendar.get(Calendar.DAY_OF_MONTH) - 7
            )

            hourCalendar.set(
                Calendar.DAY_OF_MONTH,
                hourCalendar.get(Calendar.DAY_OF_MONTH) - 7
            )
        } else {
            mainCalendar.set(
                Calendar.DAY_OF_MONTH,
                mainCalendar.get(Calendar.DAY_OF_MONTH) + 7
            )

            hourCalendar.set(
                Calendar.DAY_OF_MONTH,
                hourCalendar.get(Calendar.DAY_OF_MONTH) + 7
            )
        }

        clearEvents()
        getWeekDay()
        mWeeklyHourAdapter.setData(getDayHour())
    }

    private fun getDayHour(): ArrayList<WeeklyHourVO> {
        val hourList = arrayListOf<WeeklyHourVO>()
        for (value in 0..23) {
            val hour = if (value < 10) {
                "0$value"
            } else {
                "$value"
            }

            val monthBeginningCell: Int = hourCalendar.get(Calendar.DAY_OF_WEEK) - 1
            hourCalendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

            var count = 0
            while (count < 8) {
                if (count > 0) {
                    hourList.add(
                        WeeklyHourVO(
                            date = ymdFormatter.format(hourCalendar.time),
                            hour = hour,
                            isShow = false
                        )
                    )
                    hourCalendar.add(Calendar.DAY_OF_MONTH, 1)
                } else {
                    hourList.add(WeeklyHourVO(hour = hour, isShow = true))
                }
                count += 1
            }
            hourCalendar.set(
                Calendar.DAY_OF_MONTH,
                hourCalendar.get(Calendar.DAY_OF_MONTH) - 7
            )
        }
        return hourList
    }

    @SuppressLint("SetTextI18n")
    private fun getWeekDay() {
        val dateList = arrayListOf<String>()
        val monthBeginningCell: Int = mainCalendar.get(Calendar.DAY_OF_WEEK) - 1
        mainCalendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        val weekStartDay: String = sdfWeekDay.format(mainCalendar.time)
        weekStartDate = ymdFormatter.format(mainCalendar.time)

        while (dateList.size < 7) {
            dateList.add(ymdFormatter.format(mainCalendar.time))
            mainCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        mainCalendar.add(Calendar.DAY_OF_MONTH, -1)

        val weekEndDay: String = sdfWeekDay.format(mainCalendar.time)
        weekEndDate = ymdFormatter.format(mainCalendar.time)

        for ((position, date) in dateList.withIndex()) {
            when (position) {
                0 -> {
                    setDate(date, tvSun)
                }
                1 -> {
                    setDate(date, tvMon)
                }
                2 -> {
                    setDate(date, tvTue)
                }
                3 -> {
                    setDate(date, tvWed)
                }
                4 -> {
                    setDate(date, tvThu)
                }
                5 -> {
                    setDate(date, tvFri)
                }
                else -> {
                    setDate(date, tvSat)
                }
            }
        }

        tvDate.text = "$weekStartDay-$weekEndDay"

        if (onWeekChangeListener != null) {
            onWeekChangeListener!!.onDateChange(
                weekStartDate, weekEndDate
            )
        }
    }

    private fun setDate(date: String, tvDate: MaterialTextView) {
        tvDate.text = ymdFormatter.parse(date)?.let { dateFormat.format(it) }

        if (date == ymdFormatter.format(Date())) {
            tvDate.setBackgroundResource(R.drawable.circle_drawable)
        } else {
            tvDate.background = null
        }
    }

    fun setOnWeekChangeListener(onWeekChangeListener: OnWeekChangeListener) {
        this.onWeekChangeListener = onWeekChangeListener
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
        mWeeklyHourAdapter.setEvent(eventList)
    }

    fun clearEvents(){
        mWeeklyHourAdapter.clearEvent()
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

    fun getStartDate():String{
        return weekStartDate
    }

    fun getEndDate():String{
        return weekEndDate
    }

    fun setWeekendOff(status: Boolean) {
        weekendOff = status
        if (status) {
            lblSun.setTextColor(ContextCompat.getColor(lblSun.context, R.color.red))
            tvSun.setTextColor(ContextCompat.getColor(tvSun.context, R.color.red))
            lblSat.setTextColor(ContextCompat.getColor(lblSat.context, R.color.red))
            tvSat.setTextColor(ContextCompat.getColor(tvSat.context, R.color.red))
        }
    }

    fun setSundayOff(status: Boolean) {
        sundayOff = status
        if (status) {
            lblSun.setTextColor(ContextCompat.getColor(lblSun.context, R.color.red))
            tvSun.setTextColor(ContextCompat.getColor(tvSun.context, R.color.red))
        }
    }

    fun setLineColor(color: Int) {
        mWeeklyHourAdapter.setLineColor(color)
    }

    fun setOneTimeColor(color: Int) {
        mWeeklyHourAdapter.setOneTimeColor(color)
    }

    fun setRepeatColor(color: Int) {
        mWeeklyHourAdapter.setRepeatColor(color)
    }

    fun setProcessColor(color: Int) {
        mWeeklyHourAdapter.setProcessColor(color)
    }

    fun setHolidayEventColor(color: Int) {
        mWeeklyHourAdapter.setHolidayColor(color)
    }

    fun setPastColor(color: Int) {
        mWeeklyHourAdapter.setPastColor(color)
    }
}