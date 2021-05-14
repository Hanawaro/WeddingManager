package com.weddingManager.weddingmanager.ui.componentList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.componentList.componentsRecycler.ComponentListRecycler
import kotlinx.android.synthetic.main.fragment_componen_editor.*
import kotlinx.android.synthetic.main.fragment_component_list.*

class ComponentList : Fragment(R.layout.fragment_component_list) {

    private val args: ComponentListArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        var id: Int = 0
        activity?.title = when(ComponentModel.Type.convert(args.typeName)) {
            ComponentModel.Type.Place -> {
                id = args.wedding.place
                "Места"
            }
            ComponentModel.Type.Photographer -> {
                id = args.wedding.photographer
                "Фотографы"
            }
            ComponentModel.Type.NaT -> ""
        }

        val componentRecycler = ComponentListRecycler(requireContext(), args.wedding, ComponentModel.Type.convert(args.typeName), parentFragmentManager, recycler_view_components_list)

        Repository.Component.getAll(requireContext(), id, args.typeName, args.wedding.date).observe(viewLifecycleOwner, Observer<List<ComponentModel>> {
            componentRecycler.submitList(it)
        })

        fab_add_component.setOnClickListener {
            val action = ComponentListDirections.actionComponentListToComponentEditor(null, args.typeName)
            findNavController().navigate(action)
        }
    }

}