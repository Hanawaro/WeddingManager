package com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.MenuViewModel
import com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator.ActiveDayDecorator
import com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator.CurrentDayDecorator
import com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator.NotActiveDayDecorator
import com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator.EveryoneDecorator
import com.weddingManager.weddingmanager.util.TimeRange
import com.weddingManager.weddingmanager.util.asCalendarDay
import kotlinx.coroutines.flow.MutableStateFlow

class CalendarBottomSheet(context: Context, searchQuery: MutableStateFlow<MenuViewModel.SearchUnit>) {

    private val controller = CalendarController(searchQuery)

    private val timeRange = TimeRange()
    private val currentDayDecorator = CurrentDayDecorator(timeRange.current.asCalendarDay())
    private val activeDayDecorator = ActiveDayDecorator()
    private val notActiveDayDecorator = NotActiveDayDecorator()

    private var dialog: BottomSheetDialog
    private var widget: MaterialCalendarView? = null

    init {
        dialog = BottomSheetDialog(context).apply {

            setContentView(R.layout.layout_bottom_sheet)

            widget = findViewById<MaterialCalendarView>(R.id.bottom_sheet_calendar_view)?.apply {
                currentDate = timeRange.current.asCalendarDay()
                state().edit().isCacheCalendarPositionEnabled(true).commit()

                addDecorator(currentDayDecorator)
                addDecorator(activeDayDecorator)
                addDecorator(notActiveDayDecorator)
                addDecorator(EveryoneDecorator(context))

                setOnDateChangedListener(controller)
                setOnRangeSelectedListener(controller)
            }
        }
        val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {  }
        })
    }

    fun filter(list: List<WeddingModel>) {
        activeDayDecorator.data = list.filter { it.date >= timeRange.current.timeInMillis / 1000L }
        notActiveDayDecorator.data = list.filter { it.date < timeRange.current.timeInMillis  / 1000L }
    }

    fun show() {
        dialog.show()
    }

    fun invalidate() {
        widget?.invalidateDecorators()
    }

}