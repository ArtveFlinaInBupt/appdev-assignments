package com.artveflina.naiveclock.util

import android.icu.util.Calendar
import android.icu.util.TimeZone

class DatetimeGetter {
    private var timeZone: TimeZone

    constructor() : this(TimeZone.getDefault())

    constructor(timeZone: TimeZone) {
        this.timeZone = timeZone
    }

    fun currentDatetime(): List<Int> {
        val calendar = Calendar.getInstance(timeZone)
        return listOf(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY), /* 24 hours format (0 ~ 23)*/
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
    }

    fun currentTime(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance(timeZone)
        return Triple(
            calendar.get(Calendar.HOUR_OF_DAY), /* 24 hours format (0 ~ 23)*/
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
    }

    fun currentTimeWithMillis(): Triple<Int, Int, Double> {
        val calendar = Calendar.getInstance(timeZone)
        return Triple(
            calendar.get(Calendar.HOUR_OF_DAY), /* 24 hours format (0 ~ 23)*/
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND) / 1000.0,
        )
    }
}
