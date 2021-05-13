package com.weddingManager.weddingmanager.util

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.components.weddingsRecycler.WeddingAdapter

open class ImageDialog() : DialogFragment() {

    var isFixed = false
        private set

    var bitmap: Bitmap? = null

    var callback: (() -> Unit) = {  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.dialog_no_border)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_image_view, container);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFixed = false

        view.findViewById<ImageView>(R.id.show_image_view).setImageBitmap(bitmap)

        view.findViewById<ImageView>(R.id.show_image_view)?.setOnClickListener {
            WeddingAdapter.isVerticallyScrolling = true
            callback()
            dismiss()
        }
    }

    fun fix() {
        isFixed = true
    }

}