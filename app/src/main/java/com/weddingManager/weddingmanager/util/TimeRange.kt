package com.weddingManager.weddingmanager.util

import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class TimeRange {

    constructor() {
        this.from = Calendar.getInstance().apply {
            time = Date(0)
        }

        this.to = Calendar.getInstance().apply {
            time = Date(Long.MAX_VALUE)
        }
    }

    constructor(from: CalendarDay) {
        this.from = from.asCalendar(-12)
        this.to = from.asCalendar(6)
    }

    constructor(from: CalendarDay, to: CalendarDay) {
        this.from = from.asCalendar(-12)
        this.to = to.asCalendar(6)
    }

    val from: Calendar
    val to: Calendar

    val current: Calendar = Calendar.getInstance().apply {
        time = Date(System.currentTimeMillis())
        set(Calendar.HOUR, 3)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        Log.d("LOGGING_DATE", "${time.time}")
    }
}