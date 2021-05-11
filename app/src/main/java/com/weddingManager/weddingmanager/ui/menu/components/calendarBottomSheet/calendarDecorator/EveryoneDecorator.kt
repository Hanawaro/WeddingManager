package com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.weddingManager.weddingmanager.R

class EveryoneDecorator(private val context: Context) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        ResourcesCompat.getDrawable(context.resources, R.drawable.mcv_each_element, null)?.let { view?.setSelectionDrawable(it) }
    }

}