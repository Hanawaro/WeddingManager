package com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.util.asCalendarDay
import java.util.*
import kotlin.collections.ArrayList

class ActiveDayDecorator(var data: List<WeddingModel> = ArrayList()) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return data.stream().anyMatch {
            val calendar = Calendar.getInstance().apply {
                time = Date(it.date * 1000)
            }

            return@anyMatch day == calendar.asCalendarDay()
        }
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(  Color.RED))
    }

}