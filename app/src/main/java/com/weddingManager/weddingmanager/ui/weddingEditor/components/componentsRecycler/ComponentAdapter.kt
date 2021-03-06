package com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.weddingEditor.WeddingEditorDirections
import kotlinx.android.synthetic.main.item_wedding_component.view.*
import kotlinx.android.synthetic.main.item_wedding_component_with_title.view.*
import kotlin.math.abs

class ComponentAdapter(
        private val wedding: WeddingModel,
        private val fragmentManager: FragmentManager,
        private val canScroll: MutableLiveData<Boolean>

        ) : ListAdapter<ComponentModel, ComponentAdapter.ComponentViewHolder>(DiffCallback()) {

    companion object {
        var isVerticallyScrolling = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wedding_component_with_title, parent, false)
        return ComponentViewHolder(wedding, fragmentManager, canScroll, itemView)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ComponentViewHolder(
            private val wedding: WeddingModel,
            private val fragmentManager: FragmentManager,
            private val canScroll: MutableLiveData<Boolean>,
            itemView: View

            ) : RecyclerView.ViewHolder(itemView) {

        private val infoDialog = InfoDialog().apply {
            callback = {
                canScroll.postValue(true)
            }
        }

        fun bind(component: ComponentModel) {
            itemView.apply {
                wedding_component_title.text = when(ComponentModel.Type.convert(component.type)) {
                    ComponentModel.Type.Place -> "?????????? ????????????????????"
                    ComponentModel.Type.Photographer -> "?????? ????????????????"
                    ComponentModel.Type.NaT -> ""
                }
                if (component.name.isEmpty()) {
                    wedding_component_name.visibility = View.INVISIBLE
                    wedding_component_image.visibility = View.INVISIBLE

                    wedding_component_hello_message.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    wedding_component_hello_message.text = "???? ??????????????"
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

                            canScroll.postValue(true)
                            isLongClick = false
                        }
                    }

                    return@setOnTouchListener false
                }

                wedding_component_container.setOnLongClickListener {
                    isLongClick = true
                    // show image here

                    canScroll.postValue(false)

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

                wedding_component_container.setOnClickListener { it ->
                    val duration = 300L
                    val scale = 1.1f

                    it.animate().setDuration(duration / 2).scaleX(scale).scaleY(scale).withEndAction {
                        it.animate().setDuration(duration / 2).scaleX(1f).scaleY(1f).setInterpolator(
                                OvershootInterpolator()
                        ).start()
                    }.start()

                    if (wedding.date == WeddingModel.noDateValue) {
                        Toast.makeText(context, "?????????????? ????????", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (wedding.id != 0) {
                        Repository.Wedding.getAll(context, wedding.id).observe(wedding_component_container.findViewTreeLifecycleOwner()!!, androidx.lifecycle.Observer { list ->
                            list.first().apply {
                                husbandName = wedding.husbandName
                                wifeName = wedding.wifeName
                                date = wedding.date
                                photo = wedding.photo
                                when (ComponentModel.Type.convert(component.type)) {
                                    ComponentModel.Type.Place -> {
                                        photographer = wedding.photographer
                                    }
                                    ComponentModel.Type.Photographer -> {
                                        place = wedding.place
                                    }
                                    ComponentModel.Type.NaT -> {
                                    }
                                }
                            }
                            val action = WeddingEditorDirections.actionWeddingEditorToComponentList(component.type, list.first())
                            Navigation.findNavController(itemView).navigate(action)
                        })
                    } else {
                        val action = WeddingEditorDirections.actionWeddingEditorToComponentList(component.type, wedding)
                        Navigation.findNavController(itemView).navigate(action)
                    }

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