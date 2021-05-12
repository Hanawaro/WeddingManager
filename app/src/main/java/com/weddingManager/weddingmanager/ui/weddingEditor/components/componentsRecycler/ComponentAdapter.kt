package com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler

import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.components.weddingsRecycler.WeddingAdapter
import com.weddingManager.weddingmanager.ui.weddingEditor.WeddingEditorDirections
import kotlinx.android.synthetic.main.item_wedding_component.view.*
import java.awt.font.TextAttribute
import kotlin.math.abs

class ComponentAdapter(private val fragmentManager: FragmentManager) : ListAdapter<ComponentModel, ComponentAdapter.ComponentViewHolder>(DiffCallback()) {

    companion object {
        var isVerticallyScrolling = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wedding_component, parent, false)
        return ComponentViewHolder(fragmentManager, itemView)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ComponentViewHolder(private val fragmentManager: FragmentManager, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val infoDialog = InfoDialog()

        fun bind(component: ComponentModel) {
            itemView.apply {
                wedding_component_title.text = component.type
                if (component.name.isEmpty()) {
                    wedding_component_name.visibility = View.INVISIBLE
                    wedding_component_image.visibility = View.INVISIBLE

                    wedding_component_hello_message.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    wedding_component_hello_message.text = "не выбрано"
                } else {
                    wedding_component_name.text = component.name

                    wedding_component_name.visibility = View.VISIBLE
                    wedding_component_image.visibility = View.VISIBLE
                    wedding_component_hello_message.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    wedding_component_hello_message.text = ""

                    wedding_component_image.setImageBitmap(BitmapFactory.decodeByteArray(component.photo, 0, component.photo.size))
                    wedding_component_image.layoutParams.width = 100 * Resources.getSystem().displayMetrics.density.toInt()
                    wedding_component_image.layoutParams.height = wedding_component_image.layoutParams.width
                }

                var isLongClick = false

                var x = 0f
                var y = 0f

                wedding_component_container.setOnTouchListener { _, event ->
                    when(event.action) {
                        MotionEvent.ACTION_MOVE -> {
                            if (isLongClick && abs(x - event.rawX) > 100 || abs(y - event.rawY) > 100) {
                                infoDialog.fix()
                            }
                        }
                        MotionEvent.ACTION_DOWN -> {
                            x = event.rawX
                            y = event.rawY
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!isLongClick) {
                                performClick()
                            } else if (component.name.isNotEmpty() && !infoDialog.isFixed) {
                                // close image here
                                infoDialog.dismiss()
                            }
//
//                            WeddingAdapter.isVerticallyScrolling = true
                            isLongClick = false
                        }
                    }

                    return@setOnTouchListener false
                }

                wedding_component_container.setOnLongClickListener {
                    isLongClick = true
                    // show image here

                    WeddingAdapter.isVerticallyScrolling = false

                    if (component.name.isNotEmpty()) {
                        infoDialog.apply {
                            bitmap = BitmapFactory.decodeByteArray(component.photo, 0, component.photo.size)
                            name = component.name
                            info = component.info
                        }.show(fragmentManager, "")
                    } else {
                        val timeAnimation = 300L
                        val scale = 0.85f

                        it.animate()
                                .setDuration(timeAnimation / 3)
                                .scaleX(scale)
                                .scaleY(scale)
                                .withEndAction {
                                    it.animate()
                                            .setDuration(timeAnimation / 3 * 2)
                                            .setInterpolator(OvershootInterpolator())
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .start()
                                }.start()
                    }

                    return@setOnLongClickListener true

                }

                wedding_component_container.setOnClickListener {
                    val duration = 300L
                    val scale = 1.1f

                    it.animate().setDuration(duration / 2).scaleX(scale).scaleY(scale).withEndAction {
                        it.animate().setDuration(duration / 2).scaleX(1f).scaleY(1f).setInterpolator(
                                OvershootInterpolator()
                        ).start()
                    }.start()

                    val action = WeddingEditorDirections.actionWeddingEditorToComponentList(component.type)
                    Navigation.findNavController(itemView).navigate(action)

                }

            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ComponentModel>() {
        override fun areItemsTheSame(oldItem: ComponentModel, newItem: ComponentModel): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ComponentModel, newItem: ComponentModel): Boolean =
                oldItem == newItem
    }
}