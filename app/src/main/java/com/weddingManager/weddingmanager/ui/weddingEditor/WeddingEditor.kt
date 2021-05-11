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
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import kotlinx.android.synthetic.main.fragment_wedding_editor.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class WeddingEditor : Fragment(R.layout.fragment_wedding_editor) {

    private val args: WeddingEditorArgs by navArgs()

    private lateinit var wedding: WeddingModel
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        activity?.title = "Свадебный редактор"


        if (args.wedding != null) {

            wedding = args.wedding!!

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

            if (wedding.date == Long.MAX_VALUE)
                wedding_editor_date.text = "дата не установлена"
            else
                wedding_editor_date.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(
                    Date(
                        wedding.date * 1000
                    )
                )
        } else {
            wedding = WeddingModel(
                "",
                ""
            )
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                val inputStream = requireContext().contentResolver.openInputStream(data?.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)

                val byteArray = stream.toByteArray()

                if (byteArray.size < 1024 * 1024) {
                    wedding.photo = byteArray
                    wedding_editor_image.setImageBitmap(bitmap)
                } else {
                    Snackbar.make(requireView(), "Too large image", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        fab_save_wedding_editor.setOnClickListener {
            wedding.husbandName = wedding_editor_husband.text.toString()
            wedding.wifeName = wedding_editor_wife.text.toString()

            if (wedding.husbandName.isNotEmpty() && wedding.wifeName.isNotEmpty()) {
                Repository.Wedding.update(requireContext(), wedding)
                // go to menu

                val action = WeddingEditorDirections.actionWeddingEditorToMenu()
                findNavController().navigate(action)

                Snackbar.make(requireView(), "Data has saved", Snackbar.LENGTH_LONG).show()
            } else {
                // stay here and

                Snackbar.make(requireView(), "Fields are empty", Snackbar.LENGTH_LONG).show()
            }
        }

        wedding_editor_date.setOnClickListener {
            // show calendar dialog

            val duration = 300L
            val scale = 1.1f

            wedding_editor_date.animate().setDuration(duration / 2).scaleX(scale).scaleY(scale).withEndAction {
                wedding_editor_date.animate().setDuration(duration / 2).scaleX(1f).scaleY(1f).setInterpolator(
                    OvershootInterpolator()
                ).start()
            }.start()
        }

        wedding_editor_image.setOnClickListener {

            val duration = 300L
            val scale = 1.1f

            wedding_editor_image.animate().setDuration(duration / 2).scaleX(scale).scaleY(scale).withEndAction {
                wedding_editor_image.animate().setDuration(duration / 2).scaleX(1f).scaleY(1f).setInterpolator(
                    OvershootInterpolator()
                ).start()
            }.start()

            val intent = Intent().apply {
                type = "image/jpeg"
                action = Intent.ACTION_GET_CONTENT
            }
            resultLauncher.launch(Intent.createChooser(intent, "choose photo"))
        }

        // set recycle view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_viewer, menu)
    }
}



