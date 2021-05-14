package com.weddingManager.weddingmanager.ui.componentEditor

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.componentList.ComponentListArgs
import kotlinx.android.synthetic.main.fragment_componen_editor.*

class ComponentEditor : Fragment(R.layout.fragment_componen_editor) {

    private val args: ComponentEditorArgs by navArgs()

    lateinit var component: ComponentModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        var id: Int = 0
        activity?.title = when(ComponentModel.Type.convert(args.type)) {
            ComponentModel.Type.Place -> "Место"
            ComponentModel.Type.Photographer -> "Фотограф"
            ComponentModel.Type.NaT -> ""
        }

        if (args.component != null) {
            component = args.component!!
            if (args.component!!.photo.isNotEmpty())
                component_editor_info_image.setImageBitmap(BitmapFactory.decodeByteArray(args.component!!.photo, 0, args.component!!.photo.size))
            component_editor_info_name.setText(args.component!!.name)
            component_editor_info_content.setText(args.component!!.info)
        } else {
           component = ComponentModel("", "", args.type, ByteArray(0))
        }



        fab_save_component.setOnClickListener {

        }
    }

}