package com.weddingManager.repository

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.weddingManager.database.Database
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.DateModel
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

//  Repository.Wedding.deleteAll(requireContext())

//    val bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.profile);
//    val stream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
//    val image: ByteArray = stream.toByteArray()
//
//    Repository.Wedding?.insert(requireContext(), WeddingModel(
//        "Имя мужа",
//        "Имя жены",
//        image,
//        calendarBottomSheet.timeRange.current.timeInMillis / 1000L,
//        0, 0
//    ))


object Repository {

    var database: Database? = null

    internal fun getDatabase(context: Context): Database = Database.get(context)

    object Wedding {

        fun insert(context: Context, wedding: WeddingModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getWeddingDAO().insert(wedding)
            }
        }

        fun update(context: Context, wedding: WeddingModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getWeddingDAO().update(wedding)
            }
        }

        fun delete(context: Context, wedding: WeddingModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getWeddingDAO().delete(wedding)
            }
        }

        fun deleteAll(context: Context) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getWeddingDAO().deleteAll()
            }
        }

        fun getAll(context: Context, regex: String = ""): LiveData<List<WeddingModel>> {
            database = getDatabase(context)
            return database!!.getWeddingDAO().getAll(regex).asLiveData()
        }

    }

    object Component {

        fun insert(context: Context, component: ComponentModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getComponentDAO().insert(component)
            }
        }

        fun update(context: Context, component: ComponentModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getComponentDAO().update(component)
            }
        }

        fun delete(context: Context, component: ComponentModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getComponentDAO().delete(component)
            }
        }

        fun getAll(context: Context, type: ComponentModel.Type): LiveData<List<ComponentModel>> {
            database = getDatabase(context)
            return database!!.getComponentDAO().getAll(type.type)
        }

    }

    object Date {

        fun insert(context: Context, date: DateModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getDateDAO().insert(date)
            }
        }

        fun update(context: Context, date: DateModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getDateDAO().update(date)
            }
        }

        fun delete(context: Context, date: DateModel) {
            database = getDatabase(context)
            CoroutineScope(IO).launch {
                database!!.getDateDAO().delete(date)
            }
        }

        fun getAll(context: Context, component: ComponentModel): LiveData<List<DateModel>> {
            database = getDatabase(context)
            return database!!.getDateDAO().getAll(component.id)
        }

    }

}