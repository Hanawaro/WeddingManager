package com.weddingManager.weddingmanager.ui.componentList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.weddingEditor.WeddingEditorViewModel
import kotlinx.android.synthetic.main.fragment_component_list.*

class ComponentList : Fragment(R.layout.fragment_component_list) {

    private val args: ComponentListArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        set_0.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(args.typeName, 0)
        }

        set_1.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(args.typeName, 1)
        }
    }

}