package com.weddingManager.weddingmanager.ui.componentList.componentsRecycler

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.componentList.ComponentListDirections
import kotlinx.android.synthetic.main.item_wedding_component.view.*

class ComponentListAdapter(private val weddingModel: WeddingModel, private val type: ComponentModel.Type, private val fragmentManager: FragmentManager) : ListAdapter<ComponentModel, ComponentListAdapter.ComponentViewHolder>(DiffCallback()) {

    companion object {
        var isVerticallyScrolling = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wedding_component, parent, false)
        return ComponentViewHolder(weddingModel, type, fragmentManager, itemView)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ComponentViewHolder(private val weddingModel: WeddingModel, private val type: ComponentModel.Type, private val fragmentManager: FragmentManager, itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(component: ComponentModel) {
            itemView.apply {
                if (component.name.isEmpty()) {
                    wedding_component_item_name.visibility = View.INVISIBLE
                    wedding_component_item_image.visibility = View.INVISIBLE

                    wedding_component_item_hello_message.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    wedding_component_item_hello_message.text = "не выбрано"
                } else {
                    wedding_component_item_name.text = component.name

                    wedding_component_item_name.visibility = View.VISIBLE
                    wedding_component_item_image.visibility = View.VISIBLE
                    wedding_component_item_hello_message.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    wedding_component_item_hello_message.text = ""

                    wedding_component_item_image.setImageBitmap(BitmapFactory.decodeByteArray(component.photo, 0, component.photo.size))
                    wedding_component_item_image.layoutParams.width = 100 * Resources.getSystem().displayMetrics.density.toInt()
                    wedding_component_item_image.layoutParams.height = wedding_component_item_image.layoutParams.width
                }

                wedding_component_item_container.setOnLongClickListener {

                    val duration = 300L
                    val scale = 0.9f

                    it.animate().setDuration(duration / 2).scaleX(scale).scaleY(scale).withEndAction {
                        it.animate().setDuration(duration / 2).scaleX(1f).scaleY(1f).setInterpolator(
                                OvershootInterpolator()
                        ).start()
                    }.start()

                    if (component.name.isNotEmpty()) {
                        val action = ComponentListDirections.actionComponentListToComponentEditor(component, type.type)
                        Navigation.findNavController(itemView).navigate(action)

                    }

                    return@setOnLongClickListener true
                }

                wedding_component_item_container.setOnClickListener {
                    when(type) {
                        ComponentModel.Type.Place -> weddingModel.place = component.id
                        ComponentModel.Type.Photographer -> weddingModel.photographer = component.id
                    }

                    val action = ComponentListDirections.actionComponentListToWeddingEditor(weddingModel)
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