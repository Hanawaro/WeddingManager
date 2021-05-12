package com.weddingManager.weddingmanager.util

import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class TimePicker {

    val time: Calendar

    constructor(current: CalendarDay) {
        time = current.asCalendar().apply {
            set(Calendar.HOUR, 3)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
//            Log.d("LOGGING_DATE", "${time.time}")
        }

    }

    constructor(current: Calendar) {
        time = current.apply {
            set(Calendar.HOUR, 3)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
//            Log.d("LOGGING_DATE", "${time.time}")
        }
    }

    constructor(current: Long) {
        time = Calendar.getInstance().apply {
            time = Date(current)
            set(Calendar.HOUR, 3)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
//            Log.d("LOGGING_DATE", "${time.time}")
        }
    }

}