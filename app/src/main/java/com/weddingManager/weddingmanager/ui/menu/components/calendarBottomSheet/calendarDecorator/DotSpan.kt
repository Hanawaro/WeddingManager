package com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.calendarDecorator

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class DotSpan(private val color: Int) : LineBackgroundSpan {

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        charSequence: CharSequence,
        start: Int, end: Int, lineNum: Int
    ) {
        val radius = 7f

        val oldColor = paint.color
        if (color != 0) {
            paint.color = color
        }
        val x = (left + right) * 0.75f - radius
        val y = 0f
        canvas.drawCircle(x, y , radius, paint)
        paint.color = oldColor
    }



}