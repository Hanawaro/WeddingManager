package com.weddingManager.weddingmanager.ui.weddingEditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.ui.menu.MenuViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class WeddingEditorViewModel : ViewModel() {

    var wedding = MutableLiveData<WeddingModel>()

}