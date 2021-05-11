package com.weddingManager.weddingmanager.ui.weddingEditor

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weddingManager.weddingmanager.R
import kotlinx.android.synthetic.main.fragment_wedding_editor.*
import java.text.SimpleDateFormat
import java.util.*

class WeddingEditor : Fragment(R.layout.fragment_wedding_editor) {

    private val args: WeddingEditorArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.wedding != null) {
            wedding_editor_husband.setText(args.wedding!!.husbandName)
            wedding_editor_wife.setText(args.wedding!!.wifeName)
            if (args.wedding!!.date == Long.MAX_VALUE)
                wedding_editor_date.text = "дата не установлена"
            else
                wedding_editor_date.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(args.wedding!!.date * 1000))

        }

        setHasOptionsMenu(true)
        activity?.title = "Свадебный редактор"

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_viewer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}