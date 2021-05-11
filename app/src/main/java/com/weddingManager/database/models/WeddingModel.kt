package com.weddingManager.database.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "weddings")
data class WeddingModel(

    // ============= INFO ==============

    @ColumnInfo(name = "husbandName")
    var husbandName: String,

    @ColumnInfo(name = "wifeName")
    var wifeName: String,

    @ColumnInfo(name = "photo", typeAffinity = ColumnInfo.BLOB)
    var photo: ByteArray = ByteArray(0),

    @ColumnInfo(name = "date")
    var date: Long = Long.MAX_VALUE,

    // =========== COMPONENTS ===========

    @ColumnInfo(name = "placeID")
    var place: Int = -1,

    @ColumnInfo(name = "photographerID")
    var photographer: Int = -1,

    // =============== ID ===============

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0

) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeddingModel

        if (husbandName != other.husbandName) return false
        if (wifeName != other.wifeName) return false
        if (!photo.contentEquals(other.photo)) return false
        if (date != other.date) return false
        if (place != other.place) return false
        if (photographer != other.photographer) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = husbandName.hashCode()
        result = 31 * result + wifeName.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + place
        result = 31 * result + photographer
        result = 31 * result + id
        return result
    }
}