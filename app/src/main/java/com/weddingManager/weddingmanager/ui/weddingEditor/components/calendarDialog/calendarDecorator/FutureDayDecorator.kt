package com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog.calendarDecorator

import android.content.Context
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.util.TimePicker
import com.weddingManager.weddingmanager.util.asCalendar

class FutureDayDecorator(private val context: Context) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return TimePicker(day!!).time.timeInMillis >= TimePicker(System.currentTimeMillis()).time.timeInMillis
    }

    override fun decorate(view: DayViewFacade?) {
        ResourcesCompat.getDrawable(context.resources, R.drawable.mcv_each_element, null)?.let { view?.setSelectionDrawable(it) }
    }

}