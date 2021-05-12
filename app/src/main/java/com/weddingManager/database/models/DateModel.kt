package com.weddingManager.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "dates")
data class DateModel(

    @ColumnInfo(name = "componentID")
    val component: Int,

    @ColumnInfo(name = "date")
    val date: Long,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0

) : Parcelable