package com.weddingManager.weddingmanager.ui.weddingEditor

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.DateModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.weddingEditor.components.calendarDialog.CalendarDialog
import com.weddingManager.weddingmanager.ui.weddingEditor.components.componentsRecycler.ComponentRecycler
import com.weddingManager.weddingmanager.ui.weddingEditor.components.imageController.ImageController
import kotlinx.android.synthetic.main.fragment_wedding_editor.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WeddingEditor : Fragment(R.layout.fragment_wedding_editor) {

    private val args: WeddingEditorArgs by navArgs()

    lateinit var viewModel: WeddingEditorViewModel

    private lateinit var imageController: ImageController
    private lateinit var calendarDialog: CalendarDialog

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        activity?.title = "Свадебный редактор"

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(WeddingEditorViewModel::class.java)

        viewModel.wedding.observe(viewLifecycleOwner, androidx.lifecycle.Observer { wedding ->
            wedding_editor_husband.setText(wedding.husbandName)
            wedding_editor_wife.setText(wedding.wifeName)

            if (wedding.photo.isEmpty()) {
                wedding_editor_image.setImageDrawable(ColorDrawable(Color.WHITE))
            } else {
                wedding_editor_image.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                                wedding.photo,
                                0,
                                wedding.photo.size
                        )
                )
            }

            if (wedding.date == WeddingModel.noDateValue)
                wedding_editor_date.text = "дата не установлена"
            else
                wedding_editor_date.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(wedding.date * 1000))

        })

        viewModel.canScroll.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            wedding_editor_scroll_view.setOnTouchListener { _, _ ->
                !it
            }
        })

        // SET STATE

        viewModel.wedding.value = if (args.wedding != null) args.wedding!! else WeddingModel("", "")

        Log.d("DEBUG_WEDDING_VALUE", viewModel.wedding.value.toString())


        val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.wedding.value!!.photo = imageController.callBack(requireContext(), requireView(), result) ?: viewModel.wedding.value!!.photo
        }

        imageController = ImageController(wedding_editor_image, register, viewModel.canScroll).apply {
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

                Repository.Wedding.getAll(requireContext(), viewModel.wedding.value!!.id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {

                    if (it.isNotEmpty()) {
                        val model = it[0]

                        Repository.Date.delete(requireContext(), model.place, model.date)
                        if(viewModel.wedding.value!!.place != 0)
                            Repository.Date.insert(requireContext(), DateModel(viewModel.wedding.value!!.place, viewModel.wedding.value!!.date))

                        Repository.Date.delete(requireContext(), model.photographer, model.date)
                        if(viewModel.wedding.value!!.photographer != 0)
                            Repository.Date.insert(requireContext(), DateModel(viewModel.wedding.value!!.photographer, viewModel.wedding.value!!.date))
                    }
                    Repository.Wedding.insert(requireContext(), viewModel.wedding.value!!)
                    Snackbar.make(requireView(), "Свадба сохранена", Snackbar.LENGTH_LONG).show()

                    val action = WeddingEditorDirections.actionWeddingEditorToMenu()
                    findNavController().navigate(action)
                })
            } else {
                Snackbar.make(requireView(), "Есть пустые поля", Snackbar.LENGTH_LONG).show()
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

        // db

//        Repository.Component.insert(requireContext(), ComponentModel("name 1", "info 1", "photographer", viewModel.wedding.value!!.photo))
//        Repository.Component.insert(requireContext(), ComponentModel("name 2", "info 2", "photographer", viewModel.wedding.value!!.photo))
//        Repository.Component.insert(requireContext(), ComponentModel("name 3", "info 3", "photographer", viewModel.wedding.value!!.photo))

        // set recycle view

        val recycler = ComponentRecycler(requireContext(), viewModel.wedding.value!!, parentFragmentManager, recycler_view_weddings_editor, viewModel.canScroll)

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
                        recycler.componentAdapter.notifyItemChanged(0)
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
                        recycler.componentAdapter.notifyItemChanged(1)
                    })
                }
            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_viewer, menu)
    }

}



