package com.weddingManager.weddingmanager.util

import android.util.Log
import androidx.appcompat.widget.SearchView
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })

}

