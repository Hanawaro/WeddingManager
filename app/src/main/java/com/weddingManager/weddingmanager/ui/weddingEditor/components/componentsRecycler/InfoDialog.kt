package com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.components.weddingsRecycler.WeddingAdapter

class InfoDialog : DialogFragment() {

    var isFixed = false
        private set

    var bitmap: Bitmap? = null
    var name: String = ""
    var info: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.dialog_no_border)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_info_dialog, container);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFixed = false

        view.findViewById<ImageView>(R.id.component_info_image).apply {
            setImageBitmap(bitmap)
            setOnClickListener { close() }
        }

        view.findViewById<EditText>(R.id.component_info_name).apply {
            setText(name)
            setOnClickListener { close() }
        }

        view.findViewById<EditText>(R.id.component_info_content).apply {
            setText(info)
            setOnClickListener { close() }
        }

        view.findViewById<ScrollView>(R.id.component_info_content_wrapper).setOnClickListener { close() }


        view.findViewById<ConstraintLayout>(R.id.component_info_root).setOnClickListener { close() }
        view.setOnClickListener { close() }
    }

    private fun close() {
        // TODO set scrolling to true
        dismiss()
    }

    fun fix() {
        isFixed = true
    }

}