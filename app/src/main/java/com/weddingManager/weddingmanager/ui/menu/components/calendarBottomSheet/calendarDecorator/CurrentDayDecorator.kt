package com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CurrentDayDecorator(private val currentDay: CalendarDay) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return currentDay == day
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(StyleSpan(Typeface.BOLD))
        view?.addSpan(RelativeSizeSpan(1.4f))

    }

}