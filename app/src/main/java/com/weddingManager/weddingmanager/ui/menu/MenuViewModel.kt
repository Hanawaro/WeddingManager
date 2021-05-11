package com.weddingManager.weddingmanager.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.weddingManager.database.daos.WeddingDAO
import com.weddingManager.weddingmanager.util.TimeRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MenuViewModel(private val weddingDAO: WeddingDAO) : ViewModel() {
    val searchQuery = MutableStateFlow(SearchUnit())

    private val weddingsFlow = searchQuery.flatMapLatest { weddingDAO.getAll(it.regex, it.time.from.timeInMillis / 1000L, it.time.to.timeInMillis / 1000L) }

    val weddings = weddingsFlow.asLiveData()
    val allWeddings = weddingDAO.getAll().asLiveData()

    data class SearchUnit(
        val regex: String = "",
        val time: TimeRange = TimeRange()
    )
}