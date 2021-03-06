package com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel

class ComponentRecycler(context: Context, wedding: WeddingModel, fragmentManager: FragmentManager, recyclerView: RecyclerView, canScroll: MutableLiveData<Boolean>) {

    val componentAdapter = ComponentAdapter(wedding, fragmentManager, canScroll)

    init {
        recyclerView.apply {
            adapter = componentAdapter
            layoutManager = object: LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
    }

    fun submitList(list: List<ComponentModel>) {
        componentAdapter.submitList(list)
    }

}