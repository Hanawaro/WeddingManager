package com.weddingManager.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.weddingmanager.ui.menu.MenuViewModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WeddingDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wedding: WeddingModel)

    @Update
    suspend fun update(wedding: WeddingModel)

    @Delete
    suspend fun delete(wedding: WeddingModel)

    @Query("DELETE FROM weddings")
    fun deleteAll()

    @Query("SELECT * FROM weddings WHERE (husbandName LIKE '%' || :regex || '%' OR wifeName LIKE '%' || :regex || '%') AND date >= :from AND date <= :to  ORDER BY date DESC, id DESC")
    fun getAll(regex: String = "", from: Long = Long.MIN_VALUE, to: Long = Long.MAX_VALUE) : Flow<List<WeddingModel>>

}