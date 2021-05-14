package com.weddingManager.weddingmanager.ui.menu.components.weddingsRecycler

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.CalendarBottomSheet
import com.weddingManager.weddingmanager.util.Vibrator

class WeddingRecycler(context: Context, fragmentManager: FragmentManager, recyclerView: RecyclerView) {

    private val weddingAdapter = WeddingAdapter(Vibrator.getInstance(context), fragmentManager)

    init {
        recyclerView.apply {
            adapter = weddingAdapter
            layoutManager = object: LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return WeddingAdapter.isVerticallyScrolling
                }
            }
            setHasFixedSize(true)
        }

        val swipeToDelete = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleting = weddingAdapter.currentList[viewHolder.adapterPosition]
                Repository.Wedding.delete(context, deleting)
                Snackbar.make(recyclerView, "Восстановить свадьбу",
                    Snackbar.LENGTH_LONG).setAction("назад", View.OnClickListener {
                        Repository.Wedding.insert(context, deleting)
                    }).show()
            }
        }
        ItemTouchHelper(swipeToDelete).attachToRecyclerView(recyclerView)

    }

    fun submitList(list: List<WeddingModel>) {
        weddingAdapter.submitList(list)
    }

}