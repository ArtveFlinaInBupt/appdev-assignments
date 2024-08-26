package com.artveflina.naiveclock.widget

import android.icu.util.TimeZone
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.artveflina.naiveclock.R
import com.artveflina.naiveclock.util.DatetimeGetter
import com.artveflina.naiveclock.util.WorldTimeItem

private fun convertTimeZoneStringToOffset(timeZone: String): Int {
    val offset = timeZone.split(":")
    return offset[0].substring(4).toInt() * 60 * 60 * 1000 + offset[1].toInt() * 60 * 1000
}

class WorldTimeAdapter(private val worldTimeItems: List<WorldTimeItem>) : RecyclerView.Adapter<WorldTimeAdapter.WorldTimeViewHolder>() {
    class WorldTimeViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewTimeZone: TextView = view.findViewById(R.id.textViewTimeZone)
        val textViewTime: TextView = view.findViewById(R.id.textViewTime)
    }

    private val updateTimeHandler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            worldTimeItems.indices.forEach {
                notifyItemChanged(it)
            }
            updateTimeHandler.postDelayed(this, 1000)
        }
    }

    private val timeZones: List<TimeZone> = worldTimeItems.map {
        TimeZone.getTimeZone(
            TimeZone.getAvailableIDs(convertTimeZoneStringToOffset(it.timeZone))[0]
        )
    }

    override fun getItemCount() = worldTimeItems.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        updateTimeHandler.postDelayed(updateRunnable, 1000)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        updateTimeHandler.removeCallbacks(updateRunnable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorldTimeViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.world_time_item, parent, false) as ViewGroup
    )

    override fun onBindViewHolder(holder: WorldTimeViewHolder, position: Int) {
        val item = worldTimeItems[position]
        holder.textViewName.text = item.name
        holder.textViewTimeZone.text = item.timeZone
        updateView(holder, position)
    }

    private fun updateView(holder: WorldTimeViewHolder, position: Int) {
        val datetimeGetter = DatetimeGetter(timeZones[position])
        val datetime = datetimeGetter.currentDatetime()
        holder.textViewTime.text =
                String.format("%d/%d %02d:%02d", datetime[1], datetime[2], datetime[3], datetime[4])
    }
}
