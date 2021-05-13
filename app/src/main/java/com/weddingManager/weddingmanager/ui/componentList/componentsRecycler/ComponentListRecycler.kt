package com.weddingManager.weddingmanager.ui.componentList.componentsRecycler

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel

class ComponentListRecycler(context: Context, weddingModel: WeddingModel, private val type: ComponentModel.Type, fragmentManager: FragmentManager, recyclerView: RecyclerView) {

    private val componentAdapter = ComponentListAdapter(weddingModel, type, fragmentManager)

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
        val insert = ArrayList<ComponentModel>().apply {
            add(ComponentModel("", "", type.type, ByteArray(0)))
            addAll(list)
        }
        componentAdapter.submitList(insert)
    }

}