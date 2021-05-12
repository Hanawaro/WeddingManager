package com.weddingManager.weddingmanager.ui.weddingEditor

import android.R.attr.mimeType
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.MenuViewModel
import com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog.CalendarDialog
import com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler.ComponentRecycler
import com.weddingManager.weddingmanager.ui.weddingEditor.components.imageController.ImageController
import kotlinx.android.synthetic.main.fragment_wedding_editor.*
import kotlinx.coroutines.flow.flatMapConcat
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WeddingEditor : Fragment(R.layout.fragment_wedding_editor) {

    private val args: WeddingEditorArgs by navArgs()

    lateinit var viewModel: WeddingEditorViewModel

    private lateinit var imageController: ImageController
    private lateinit var calendarDialog: CalendarDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        activity?.title = "Свадебный редактор"

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(WeddingEditorViewModel::class.java)

        viewModel.wedding.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            wedding_editor_husband.setText(it.husbandName)
            wedding_editor_wife.setText(it.wifeName)

            if (it.photo.isEmpty()) {
                wedding_editor_image.setImageDrawable(ColorDrawable(Color.WHITE))
            } else {
                wedding_editor_image.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                                it.photo,
                                0,
                                it.photo.size
                        )
                )
            }

            if (it.date == WeddingModel.noDateValue)
                wedding_editor_date.text = "дата не установлена"
            else
                wedding_editor_date.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it.date * 1000))

        })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(ComponentModel.Type.Photographer.type)?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.wedding.value!!.photographer = it
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(ComponentModel.Type.Place.type)?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.wedding.value!!.place = it
        })

        // SET STATE

        viewModel.wedding.value = if (args.wedding != null) args.wedding!! else WeddingModel("", "")

        Log.d("DEBUG_WEDDING_VALUE", viewModel.wedding.value.toString())

        val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.wedding.value!!.photo = imageController.callBack(requireContext(), requireView(), result) ?: ByteArray(0)
        }

        imageController = ImageController(wedding_editor_image, register).apply {
            setListeners(viewModel.wedding.value!!, parentFragmentManager)
        }

        calendarDialog = CalendarDialog(requireContext(), viewModel.wedding)

        wedding_editor_husband.addTextChangedListener {
            viewModel.wedding.value!!.husbandName = it.toString()
        }
        wedding_editor_wife.addTextChangedListener {
            viewModel.wedding.value!!.wifeName = it.toString()
        }

        fab_save_wedding_editor.setOnClickListener {
            if (viewModel.wedding.value!!.husbandName.isNotEmpty() && viewModel.wedding.value!!.wifeName.isNotEmpty()) {
                if (Repository.Wedding.contains(requireContext(), viewModel.wedding.value!!.id))
                    Repository.Wedding.update(requireContext(), viewModel.wedding.value!!)
                else
                    Repository.Wedding.insert(requireContext(), viewModel.wedding.value!!)

                val action = WeddingEditorDirections.actionWeddingEditorToMenu()
                findNavController().navigate(action)

                Snackbar.make(requireView(), "Data has saved", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(requireView(), "Fields are empty", Snackbar.LENGTH_LONG).show()
            }
        }

        wedding_editor_date.setOnClickListener {
            // show calendar dialog

            val duration = 300L
            val scale = 1.1f

            wedding_editor_date.animate()
                    .setDuration(duration / 2)
                    .scaleX(scale)
                    .scaleY(scale)
                    .withEndAction {
                        wedding_editor_date.animate()
                                .setDuration(duration / 2)
                                .scaleX(1f)
                                .scaleY(1f)
                                .setInterpolator(OvershootInterpolator()).withEndAction { calendarDialog.show() }
                                .start()
            }.start()
        }

        // set recycle view

        val recycler = ComponentRecycler(requireContext(), parentFragmentManager, recycler_view_weddings_editor)

        val componentsList = ArrayList<ComponentModel>().apply {
            add(ComponentModel("", "", ComponentModel.Type.Photographer.type, ByteArray(0)))
            add(ComponentModel("", "", ComponentModel.Type.Place.type, ByteArray(0)))
        }

        recycler.submitList(componentsList)

        Repository.Component.getAll(requireContext()).observe(viewLifecycleOwner, androidx.lifecycle.Observer { names ->

//            // PHOTOGRAPHER
            if (names.contains(ComponentModel.Type.Photographer.type)) {
                val id = viewModel.wedding.value!!.photographer
                if (id != 0) {
                    Repository.Component.get(requireContext(), id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        componentsList[0] = it
                        recycler.submitList(componentsList)
                        recycler.componentAdapter.notifyDataSetChanged()
                    })
                }
            }

//            // PLACE
            if (names.contains(ComponentModel.Type.Place.type)) {
                val id = viewModel.wedding.value!!.place
                if (id != 0) {
                    Repository.Component.get(requireContext(), id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        componentsList[1] = it
                        recycler.submitList(componentsList)
                        recycler.componentAdapter.notifyDataSetChanged()
                    })
                }
            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_viewer, menu)
    }

}



