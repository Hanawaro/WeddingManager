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

    @Query("DELETE FROM components")
    suspend fun deleteAll()

//    @Query("SELECT * FROM components WHERE componentType =:type AND ( ((SELECT COUNT(*) from dates WHERE dates.id = components.id AND date =:date) = 0) ) ORDER BY id DESC")
//    fun getAll(type: String, date: Long) : LiveData<List<ComponentModel>>

    @Query("SELECT * FROM components WHERE componentType =:type AND ( id =:componentID OR ((SELECT COUNT(*) from dates WHERE dates.componentID = components.id AND date =:date) = 0) ) ORDER BY id DESC")
    fun getAll(componentID: Int, type: String, date: Long) : LiveData<List<ComponentModel>>

    @Query("SELECT * FROM components WHERE id =:id")
    fun get(id: Int) : LiveData<ComponentModel>

    @Query("SELECT DISTINCT componentType FROM components")
    fun getAll() : LiveData<List<String>>

}