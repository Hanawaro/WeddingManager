package com.weddingManager.weddingmanager.ui.componentEditor

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.componentEditor.imageContoller.ImageController
import com.weddingManager.weddingmanager.ui.componentList.ComponentListArgs
import kotlinx.android.synthetic.main.fragment_componen_editor.*
import kotlinx.android.synthetic.main.fragment_wedding_editor.*

class ComponentEditor : Fragment(R.layout.fragment_componen_editor) {

    private val args: ComponentEditorArgs by navArgs()

    lateinit var component: ComponentModel

    private lateinit var imageController: ImageController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

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

        val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            component.photo = imageController.callBack(requireContext(), requireView(), result) ?: component.photo
        }

        imageController = ImageController(component_editor_info_image, register).apply {
            setListeners(component, parentFragmentManager)
        }

        component_editor_info_name.addTextChangedListener {
            component.name = it.toString()
        }

        component_editor_info_content.addTextChangedListener {
            component.info = it.toString()
        }

        fab_save_component.setOnClickListener {
            if (component.photo.isNotEmpty() && component.name.isNotEmpty() && component.info.isNotEmpty()) {
                Repository.Component.insert(requireContext(), component)
                findNavController().navigateUp()
            } else {
                Snackbar.make(requireView(), "Введи все данные", Snackbar.LENGTH_LONG).show()
            }
        }
    }

}