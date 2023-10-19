package com.nhlynn.plan_calendar.adapter.weekly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.adapter.EventAdapter
import com.nhlynn.plan_calendar.databinding.WeeklyHourBinding
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnTimeClickListener
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.model.WeeklyHourVO

class WeeklyHourAdapter(
    private val listener: OnTimeClickListener,
    private val onEventClickListener: OnEventClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var weeklyHourList = arrayListOf<WeeklyHourVO>()
    private var hourEventList = arrayListOf<EventVO>()
    private var groupEventList = mapOf<String, List<EventVO>>()
    private var lineColor = R.color.line_color
    private var oneTimeColor: Int = R.color.one_time_event
    private var repeatColor: Int = R.color.repeat_event
    private var processColor: Int = R.color.progress_event
    private var holidayColor: Int = R.color.holiday_event
    private var pastColor: Int = R.color.past_event

    class WeeklyViewHolder(val viewBinder: WeeklyHourBinding) :
        RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = WeeklyHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeeklyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WeeklyViewHolder
        val root = viewHolder.viewBinder

        val item = weeklyHourList[position]

        if (item.isShow) {
            root.tvTime.text = item.hour

            root.tvTime.visibility = View.VISIBLE
            root.event.visibility = View.GONE
        } else {
            root.tvTime.visibility = View.GONE
            root.event.visibility = View.VISIBLE
        }

        root.event.setOnClickListener {
            if (item.date != null)
                listener.onClick(item.date, item.hour)
        }

        root.viewVertical.setBackgroundColor(
            ContextCompat.getColor(
                root.viewVertical.context,
                lineColor
            )
        )
        root.viewHorizontal.setBackgroundColor(
            ContextCompat.getColor(
                root.viewVertical.context,
                lineColor
            )
        )

        event(item, root.rvEvent)
    }

    override fun getItemCount(): Int {
        return weeklyHourList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(weeklyHourList: ArrayList<WeeklyHourVO>) {
        this.weeklyHourList = weeklyHourList
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

    private fun event(item: WeeklyHourVO, rvEvent: RecyclerView) {
        val eventList = arrayListOf<EventVO>()
        for (event in groupEventList) {
            if (event.key == item.hour.split(":")[0]) {
                for (hourEvent in event.value) {
                    if (hourEvent.date == item.date) {
                        eventList.add(hourEvent)
                    }
                }
            }
        }
        eventAdapter(rvEvent, eventList)
    }

    private fun eventAdapter(rvEvent: RecyclerView, event: ArrayList<EventVO>) {
        if (event.isEmpty()) {
            rvEvent.visibility = View.GONE
        } else {
            rvEvent.visibility = View.VISIBLE
        }
        val mEventAdapter = EventAdapter(onEventClickListener)
        rvEvent.layoutManager =
            LinearLayoutManager(rvEvent.context, LinearLayoutManager.HORIZONTAL, false)
        rvEvent.adapter = mEventAdapter

        mEventAdapter.setData(event)

        mEventAdapter.setOneTimeColor(oneTimeColor)
        mEventAdapter.setRepeatColor(repeatColor)
        mEventAdapter.setProcessColor(processColor)
        mEventAdapter.setHolidayColor(holidayColor)
        mEventAdapter.setPastColor(pastColor)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(eventList: ArrayList<EventVO>) {
        this.hourEventList = eventList
        groupEventList = hourEventList.groupBy { it.startTime.split(":")[0] }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearEvent(){
        groupEventList = emptyMap()
        notifyDataSetChanged()
    }
}