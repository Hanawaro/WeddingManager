package com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet

import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import com.weddingManager.weddingmanager.ui.menu.MenuViewModel
import com.weddingManager.weddingmanager.util.TimeRange
import kotlinx.coroutines.flow.MutableStateFlow

class CalendarController(private val searchQuery: MutableStateFlow<MenuViewModel.SearchUnit>) : OnRangeSelectedListener, OnDateSelectedListener {

    override fun onRangeSelected(widget: MaterialCalendarView, dates: MutableList<CalendarDay>) {
        searchQuery.value = MenuViewModel.SearchUnit(searchQuery.value.regex, TimeRange(dates.first(), dates.last()))
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        if (selected) {
            searchQuery.value = MenuViewModel.SearchUnit(searchQuery.value.regex, TimeRange(date))
            Log.d("LOG VIEW MODEL", " ${searchQuery.value.time.from.timeInMillis / 1000} / ${searchQuery.value.time.to.timeInMillis / 1000} ")
        } else {
            searchQuery.value = MenuViewModel.SearchUnit(searchQuery.value.regex)
        }
    }

}