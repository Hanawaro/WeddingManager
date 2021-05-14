package com.weddingManager.weddingmanager.ui.componentEditor.imageContoller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.weddingmanager.util.ImageDialog
import java.io.ByteArrayOutputStream
import java.lang.Math.abs

class ImageController(private val view: ImageView, private val resultLauncher: ActivityResultLauncher<Intent>) {

    private val dialog = ImageDialog()

    @SuppressLint("ClickableViewAccessibility")
    fun setListeners(component: ComponentModel, fragmentManager: FragmentManager) {

        var isLongClick = false
        var x = 0f
        var y = 0f

        view.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (isLongClick && abs(x - event.rawX) > 100 || abs(y - event.rawY) > 100) {
                        dialog.fix()
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX
                    y = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    if (isLongClick && component.photo.isNotEmpty() && !dialog.isFixed) {
                        // close image here
                        dialog.dismiss()
                    }

                    isLongClick = false
                }
            }

            return@setOnTouchListener false
        }

        view.setOnLongClickListener {
            isLongClick = true
            // show image here

            if (component.photo.isNotEmpty()) {
                dialog.bitmap = BitmapFactory.decodeByteArray(component.photo, 0, component.photo.size)
                dialog.show(fragmentManager, "")
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

        view.setOnClickListener {
            val duration = 300L
            val scale = 1.1f

            view.animate().setDuration(duration / 2).scaleX(scale).scaleY(scale).withEndAction {
                view.animate().setDuration(duration / 2).scaleX(1f).scaleY(1f).setInterpolator(
                        OvershootInterpolator()
                ).start()
            }.start()

            val intent = Intent().apply {
                type = "image/jpeg"
                action = Intent.ACTION_GET_CONTENT
            }
            resultLauncher.launch(Intent.createChooser(intent, "choose photo"))
        }
    }


    fun callBack(context: Context, requiredView: View, result: ActivityResult): ByteArray? {
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val inputStream = context.contentResolver.openInputStream(data?.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)

            val byteArray = stream.toByteArray()

            if (byteArray.size < 1024 * 1024) {
                view.setImageBitmap(bitmap)
                return byteArray
            } else {
                Snackbar.make(requiredView, "Картинка слишком большая", Snackbar.LENGTH_LONG).show()
            }
        }
        return null
    }

}