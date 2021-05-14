package com.weddingManager.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.weddingManager.database.models.DateModel

@Dao
interface DateDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date: DateModel)

    @Update
    suspend fun update(date: DateModel)

    @Delete
    suspend fun delete(date: DateModel)

    @Query("DELETE FROM dates WHERE componentID =:id AND date=:date")
    suspend fun delete(id: Int, date: Long)

    @Query("SELECT * FROM dates WHERE id =:id")
    fun getAll(id: Int) : LiveData<List<DateModel>>

}