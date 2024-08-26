package com.artveflina.naiveclock.widget

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artveflina.naiveclock.R
import com.artveflina.naiveclock.util.WorldTimeItem

class WorldClockFragment : Fragment() {
    companion object {
        private val WORLD_TIME_ITEMS = listOf(
            WorldTimeItem("Beijing", "UTC +08:00"),
            WorldTimeItem("Linyi", "UTC +08:00"),
            WorldTimeItem("Los Angeles", "UTC -08:00"),
            WorldTimeItem("Vancouver", "UTC -08:00"),
            WorldTimeItem("Chicago", "UTC -06:00"),
            WorldTimeItem("Mexico City", "UTC -06:00"),
            WorldTimeItem("New York", "UTC -05:00"),
            WorldTimeItem("Toronto", "UTC -05:00"),
            WorldTimeItem("São Paulo", "UTC -03:00"),
            WorldTimeItem("Rio de Janeiro", "UTC -03:00"),
            WorldTimeItem("London", "UTC +00:00"),
            WorldTimeItem("Paris", "UTC +01:00"),
            WorldTimeItem("Berlin", "UTC +01:00"),
            WorldTimeItem("Cairo", "UTC +02:00"),
            WorldTimeItem("Cape Town", "UTC +02:00"),
            WorldTimeItem("Moscow", "UTC +03:00"),
            WorldTimeItem("Istanbul", "UTC +03:00"),
            WorldTimeItem("Dubai", "UTC +04:00"),
            WorldTimeItem("Mumbai", "UTC +05:30"),
            WorldTimeItem("New Delhi", "UTC +05:30"),
            WorldTimeItem("Bangkok", "UTC +07:00"),
            WorldTimeItem("Hong Kong", "UTC +08:00"),
            WorldTimeItem("Beijing", "UTC +08:00"),
            WorldTimeItem("Shanghai", "UTC +08:00"),
            WorldTimeItem("Singapore", "UTC +08:00"),
            WorldTimeItem("Seoul", "UTC +09:00"),
            WorldTimeItem("Tokyo", "UTC +09:00"),
            WorldTimeItem("Sydney", "UTC +10:00"),
            WorldTimeItem("Auckland", "UTC +12:00")
        )
    }

    private lateinit var adapter: WorldTimeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.world_time_list, container, false)
        recyclerView = view.findViewById(R.id.WorldTimeList)
        adapter = WorldTimeAdapter(WORLD_TIME_ITEMS)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }
}
