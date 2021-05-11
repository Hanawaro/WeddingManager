package com.weddingManager.weddingmanager.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class TimePicker(current: CalendarDay) {

    val current: Calendar

    init {
        this.current = current.asCalendar(-12)
    }


}