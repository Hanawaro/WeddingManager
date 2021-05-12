package com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog.calendarDecorator.FutureDayDecorator
import com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog.calendarDecorator.PastDayDecorator
import com.weddingManager.weddingmanager.util.TimePicker
import com.weddingManager.weddingmanager.util.asCalendarDay

class CalendarDialog(context: Context, private val weddingModel: MutableLiveData<WeddingModel>) : DialogFragment() {

    private val controller = CalendarController(weddingModel)

    private var dialog: BottomSheetDialog
    private var widget: MaterialCalendarView? = null

    init {
        dialog = BottomSheetDialog(context).apply {

            setContentView(R.layout.layout_calendar_view)

            val time = TimePicker(weddingModel.value!!.date).time

            widget = findViewById<MaterialCalendarView>(R.id.show_calendar_view)?.apply {
                if (weddingModel.value!!.date != WeddingModel.noDateValue) {
                    currentDate = time.asCalendarDay()
                    selectedDate = time.asCalendarDay()
                } else {
                    currentDate = TimePicker(System.currentTimeMillis()).time.asCalendarDay()
                    selectedDate = TimePicker(System.currentTimeMillis()).time.asCalendarDay()
                }

                state().edit().isCacheCalendarPositionEnabled(true).commit()

                addDecorator(PastDayDecorator(context))
                addDecorator(FutureDayDecorator(context))

                setOnDateChangedListener(controller)
                clearSelection()
            }
        }
    }

    fun show() {
        dialog.show()
    }

    fun invalidate() {
        widget?.invalidateDecorators()
    }

}