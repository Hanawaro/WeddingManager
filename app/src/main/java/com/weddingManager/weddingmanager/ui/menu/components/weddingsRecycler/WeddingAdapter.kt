package com.weddingManager.weddingmanager.ui.menu.components.weddingsRecycler

import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Vibrator
import android.transition.Visibility
import android.util.Log
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.MenuDirections
import kotlinx.android.synthetic.main.fragment_wedding_editor.view.*
import kotlinx.android.synthetic.main.item_wedding.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class WeddingAdapter(private val vibrator: Vibrator?, private val fragmentManager: FragmentManager) : ListAdapter<WeddingModel, WeddingAdapter.WeddingViewHolder>(DiffCallback()) {

    companion object {
        var isVerticallyScrolling = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeddingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wedding, parent, false)
        return WeddingViewHolder(vibrator, fragmentManager, itemView)
    }

    override fun onBindViewHolder(holder: WeddingViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem)
    }

    class WeddingViewHolder(private val vibrator: Vibrator?, private val fragmentManager: FragmentManager, itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageDialog = ImageDialog()

        fun bind(wedding: WeddingModel) {
            itemView.apply {
                item_husband_name.text = wedding.husbandName
                item_wife_name.text = wedding.wifeName

                if (wedding.date == Long.MAX_VALUE) {
                    item_date.text = "дата не выбрана"
                } else {
                    val text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(wedding.date * 1000)) +
                            " | ${wedding.date.toString()}" +
                            ""
                    item_date.text = text
                }

                val calendar = Calendar.getInstance().apply {
                    time = Date(System.currentTimeMillis())
                    set(Calendar.HOUR, -12)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }

                if (wedding.date < calendar.timeInMillis / 1000L) {
                    item_root.foreground = ColorDrawable(0x55000000)
                    Log.d("TIME", (calendar.timeInMillis / 1000L).toString())
                } else {
                    item_root.foreground = ColorDrawable(0x00000000)
                }

                var isLongClick = false

                setOnClickListener{
                    val action = MenuDirections.actionMenuToWeddingEditor(wedding)
                    Navigation.findNavController(itemView).navigate(action)
                }

                setOnLongClickListener {
                    isLongClick = true
                    // show image here

                    isVerticallyScrolling = false

                    if (wedding.photo.isNotEmpty()) {
                        com.weddingManager.weddingmanager.util.Vibrator.vibrate(vibrator, 70)
                        imageDialog.bitmap = BitmapFactory.decodeByteArray(wedding.photo, 0, wedding.photo.size)
                        imageDialog.show(fragmentManager, "")

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

                        com.weddingManager.weddingmanager.util.Vibrator.vibrate(vibrator, 50)
                    }



                    return@setOnLongClickListener true
                }

                var x = 0f
                var y = 0f

                setOnTouchListener { _, event ->
                    when(event.action) {
                        MotionEvent.ACTION_MOVE -> {
                            if (isLongClick && abs(x - event.rawX) > 100 || abs(y - event.rawY) > 100) {
                                imageDialog.fix()
                            }
                        }
                        MotionEvent.ACTION_DOWN -> {
                            x = event.rawX
                            y = event.rawY
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!isLongClick) {
                                performClick()
                            } else if (wedding.photo.isNotEmpty() && !imageDialog.isFixed) {
                                // close image here
                                imageDialog.dismiss()
                            }

                            isVerticallyScrolling = true
                            isLongClick = false
                        }
                    }

                    return@setOnTouchListener false
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WeddingModel>() {
        override fun areItemsTheSame(oldItem: WeddingModel, newItem: WeddingModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: WeddingModel, newItem: WeddingModel): Boolean =
            oldItem == newItem
    }

}