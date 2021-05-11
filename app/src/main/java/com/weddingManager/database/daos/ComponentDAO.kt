package com.weddingManager.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.weddingManager.database.models.ComponentModel

@Dao
interface ComponentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(component: ComponentModel)

    @Update
    suspend fun update(component: ComponentModel)

    @Delete
    suspend fun delete(component: ComponentModel)

    @Query("SELECT * FROM components WHERE componentType =:type")
    fun getAll(type: String) : LiveData<List<ComponentModel>>

}