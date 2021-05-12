package com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.ui.menu.MenuViewModel
import com.weddingManager.weddingmanager.util.TimePicker
import com.weddingManager.weddingmanager.util.TimeRange
import kotlinx.coroutines.flow.MutableStateFlow

class CalendarController(private val weddingModel: MutableLiveData<WeddingModel>) : OnDateSelectedListener {

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        if (selected) {
            weddingModel.value!!.date = TimePicker(date).time.timeInMillis / 1000L
        } else {
            weddingModel.value!!.date = Long.MAX_VALUE
        }
        weddingModel.postValue(weddingModel.value)
    }

}