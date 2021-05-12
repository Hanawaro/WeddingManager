package com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.util.Vibrator

class ComponentRecycler(context: Context, fragmentManager: FragmentManager, recyclerView: RecyclerView) {

    val componentAdapter = ComponentAdapter(fragmentManager)

    init {
        recyclerView.apply {
            adapter = componentAdapter
            layoutManager = object: LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }
            setHasFixedSize(true)
        }
    }

    fun submitList(list: List<ComponentModel>) {
        componentAdapter.submitList(list)
    }

}