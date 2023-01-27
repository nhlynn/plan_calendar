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
import kotlin.collections.ArrayList

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

    private var mainCalendar: Calendar = Calendar.getInstance()
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
            mainCalendar.set(
                Calendar.MONTH,
                mainCalendar.get(Calendar.MONTH) - 1
            )

        } else {
            mainCalendar.set(
                Calendar.MONTH,
                mainCalendar.get(Calendar.MONTH) + 1
            )
        }

        getMonthDay()
        mMonthlyDayAdapter.setData(daysInMonthArray())
    }

    private fun getMonthDay() {
        tvDate.text = myShowFormatter.format(mainCalendar.time)

        if (onMonthChangeListener != null) {
            onMonthChangeListener!!.onMonthChange(
                ymdFormatter.format(
                    mainCalendar.time
                )
            )
        }
    }

    private fun daysInMonthArray(): ArrayList<String> {
        val daysInMonthArray: ArrayList<String> = ArrayList()
        val daysInMonth: Int = getDayInMonth()
        val dayOfWeek: Int = getDayOfWeek()

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                if (dayOfWeek < 7) {
                    daysInMonthArray.add("")
                }
            } else {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, mainCalendar.get(Calendar.YEAR))
                calendar.set(Calendar.MONTH, mainCalendar.get(Calendar.MONTH))
                calendar.set(Calendar.DAY_OF_MONTH, (i - dayOfWeek))
                daysInMonthArray.add(ymdFormatter.format(calendar.time))
            }
        }
        if (daysInMonthArray.size == 42) {
            if (daysInMonthArray[35].isEmpty() &&
                daysInMonthArray[36].isEmpty() &&
                daysInMonthArray[37].isEmpty() &&
                daysInMonthArray[38].isEmpty() &&
                daysInMonthArray[39].isEmpty() &&
                daysInMonthArray[40].isEmpty() &&
                daysInMonthArray[41].isEmpty()
            ) {
                for (day in 41 downTo 35) {
                    daysInMonthArray.removeAt(day)
                }
            }
        }
        return daysInMonthArray
    }

    private fun getDayInMonth(): Int {
        val year = mainCalendar.get(Calendar.YEAR)
        val month = mainCalendar.get(Calendar.MONTH) + 1
        return if (month == 4 || month == 6 || month == 9 || month == 11) {
            30
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            31
        } else {
            if (year % 4 == 0) {
                29
            } else {
                28
            }
        }
    }

    private fun getDayOfWeek(): Int {
        mainCalendar.set(Calendar.DAY_OF_MONTH, 1)
        return mainCalendar.get(Calendar.DAY_OF_WEEK) - 1
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
        if(weekendOff) {
            tvSat.setTextColor(ContextCompat.getColor(tvSun.context,R.color.red))
            tvSun.setTextColor(ContextCompat.getColor(tvSun.context,R.color.red))
        }
        mMonthlyDayAdapter.setWeekendOff(status)
    }

    fun setSundayOff(status: Boolean) {
        sundayOff = status
        if(sundayOff) {
            tvSun.setTextColor(ContextCompat.getColor(tvSun.context,R.color.red))
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