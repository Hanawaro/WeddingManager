package com.weddingManager.weddingmanager.ui.weddingEditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weddingManager.database.models.WeddingModel

class WeddingEditorViewModel : ViewModel() {

    var wedding = MutableLiveData<WeddingModel>()

    var canScroll = MutableLiveData<Boolean>(true)

}