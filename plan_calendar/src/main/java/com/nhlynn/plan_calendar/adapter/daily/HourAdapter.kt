package com.nhlynn.plan_calendar.adapter.daily

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.model.HourVO
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.adapter.EventAdapter
import com.nhlynn.plan_calendar.databinding.HourCellBinding
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.delegate.OnTimeClickListener

class HourAdapter(
    private val mListener: OnTimeClickListener,
    private val onEventClickListener: OnEventClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var hourList = arrayListOf<HourVO>()
    private var hourEventList = arrayListOf<EventVO>()
    private var groupEventList = mapOf<String, List<EventVO>>()
    private var lineColor = R.color.line_color
    private var oneTimeColor: Int = R.color.one_time_event
    private var repeatColor: Int = R.color.repeat_event
    private var processColor: Int = R.color.progress_event
    private var holidayColor: Int = R.color.holiday_event
    private var pastColor: Int = R.color.past_event

    class HourViewHolder(val viewBinder: HourCellBinding) : RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HourCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as HourViewHolder
        val root = viewHolder.viewBinder

        val hour = hourList[position]
        root.tvTime.text = hour.hour

        root.event.setOnClickListener { mListener.onClick(hour.date, hour.hour) }

        event(hour, root.rvEvent)

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
    }

    private fun event(hour: HourVO, rvEvent: RecyclerView) {
        val eventList = arrayListOf<EventVO>()
        for (event in groupEventList) {
            if (event.key == hour.hour.split(":")[0]) {
                for (hourEvent in event.value) {
                    if (hourEvent.date == hour.date) {
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

    override fun getItemCount(): Int {
        return hourList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(hourList: ArrayList<HourVO>) {
        this.hourList = hourList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvent(eventList: ArrayList<EventVO>) {
        this.hourEventList = eventList
        groupEventList = hourEventList.groupBy { it.startTime.split(":")[0] }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearEvent() {
        this.hourEventList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLineColor(color: Int) {
        this.lineColor = color
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setOneTimeColor(color: Int) {
        this.oneTimeColor = color
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRepeatColor(color: Int) {
        this.repeatColor = color
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProcessColor(color: Int) {
        this.processColor = color
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setHolidayEventColor(color: Int) {
        this.holidayColor = color
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPastColor(color: Int) {
        this.pastColor = color
        notifyDataSetChanged()
    }
}