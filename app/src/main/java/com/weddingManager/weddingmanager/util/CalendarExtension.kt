package com.weddingManager.weddingmanager.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

fun Calendar.asCalendarDay(): CalendarDay {
    val day = this.get(Calendar.DAY_OF_MONTH)
    val month = this.get(Calendar.MONTH)
    val year = this.get(Calendar.YEAR)
    return CalendarDay.from(year, month + 1, day)
}

fun CalendarDay.asCalendar(offset: Int = 0): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, this.day)
    calendar.set(Calendar.MONTH, this.month - 1)
    calendar.set(Calendar.YEAR, this.year)

    calendar.set(Calendar.HOUR, offset);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    return calendar
}