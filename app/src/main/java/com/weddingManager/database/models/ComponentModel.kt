package com.weddingManager.database.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "components")
data class ComponentModel(

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "info")
    val info: String,

    @ColumnInfo(name = "componentType")
    val type: String,

    @ColumnInfo(name = "photo", typeAffinity = ColumnInfo.BLOB)
    val photo: ByteArray,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0

) : Parcelable {

    enum class Type(val type: String) {
        Place("place"), Photographer("photographer")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComponentModel

        if (name != other.name) return false
        if (type != other.type) return false
        if (!photo.contentEquals(other.photo)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + id
        return result
    }

}
