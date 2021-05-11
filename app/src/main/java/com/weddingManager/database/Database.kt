package com.weddingManager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.weddingManager.database.daos.ComponentDAO
import com.weddingManager.database.daos.DateDAO
import com.weddingManager.database.daos.WeddingDAO
import com.weddingManager.database.models.ComponentModel
import com.weddingManager.database.models.DateModel
import com.weddingManager.database.models.WeddingModel

@Database(entities = [WeddingModel::class, ComponentModel::class, DateModel::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun getWeddingDAO(): WeddingDAO
    abstract fun getComponentDAO(): ComponentDAO
    abstract fun getDateDAO(): DateDAO

    companion object {

        @Volatile
        private var INSTANCE: com.weddingManager.database.Database? = null

        fun get(context: Context) : com.weddingManager.database.Database {

            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, com.weddingManager.database.Database::class.java, "WeddingManagerDatabase")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }

    }

}