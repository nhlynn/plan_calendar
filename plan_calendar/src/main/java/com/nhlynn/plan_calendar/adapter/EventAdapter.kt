package com.nhlynn.plan_calendar.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.plan_calendar.R
import com.nhlynn.plan_calendar.databinding.EventItemBinding
import com.nhlynn.plan_calendar.delegate.OnEventClickListener
import com.nhlynn.plan_calendar.model.EventVO
import com.nhlynn.plan_calendar.utils.*
import java.util.Date

class EventAdapter(private val delegate: OnEventClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var eventList = arrayListOf<EventVO>()
    private var oneTimeColor: Int = R.color.one_time_event
    private var repeatColor: Int = R.color.repeat_event
    private var processColor: Int = R.color.progress_event
    private var holidayColor: Int = R.color.holiday_event
    private var pastColor: Int = R.color.past_event

    class EventViewHolder(val viewBinder: EventItemBinding) :
        RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as EventViewHolder
        val binding = viewHolder.viewBinder

        val event = eventList[position]

        binding.tvEventTime.text = "${event.startTime}-${event.endTime}"
        binding.tvEventName.text = event.eventName

        val color = convertColor(event.date, event.eventType, event.startTime, event.endTime)
        binding.view.setBackgroundColor(ContextCompat.getColor(binding.view.context, color))
        binding.cvCard.strokeColor = ContextCompat.getColor(binding.cvCard.context, color)

        binding.cvCard.setOnClickListener {
            delegate.eventClick(event)
        }
    }

    private fun convertColor(
        date: String, eventType: Int?, startTime: String, endTime: String
    ): Int {
        return if (ymdFormatter.parse(ymdFormatter.format(Date()))
                ?.after(ymdFormatter.parse(date)) == true
        ) {
            pastColor
        } else if (ymdFormatter.format(Date()) == date) {
            if (timeToInt(hmFormat.format(defaultCalendar.time)) > timeToInt(endTime)) {
                pastColor
            } else if (timeToInt(hmFormat.format(defaultCalendar.time)) >= timeToInt(startTime) && timeToInt(
                    hmFormat.format(defaultCalendar.time)
                ) <= timeToInt(endTime)
            ) {
                processColor
            } else {
                when (eventType) {
                    1 -> {
                        oneTimeColor
                    }
                    2 -> {
                        repeatColor
                    }
                    3 -> {
                        processColor
                    }
                    4 -> {
                        holidayColor
                    }
                    else -> {
                        R.color.white
                    }
                }
            }
        } else {
            when (eventType) {
                1 -> {
                    oneTimeColor
                }
                2 -> {
                    repeatColor
                }
                4 -> {
                    holidayColor
                }
                else -> {
                    R.color.white
                }
            }
        }
    }

    private fun timeToInt(time: String): Int {
        return (Integer.parseInt(time.split(":")[0]) * 60) + (Integer.parseInt(time.split(":")[1]))
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(eventList: ArrayList<EventVO>) {
        this.eventList = eventList
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
    fun setHolidayColor(color: Int) {
        this.holidayColor = color
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPastColor(color: Int) {
        this.pastColor = color
        notifyDataSetChanged()
    }
}