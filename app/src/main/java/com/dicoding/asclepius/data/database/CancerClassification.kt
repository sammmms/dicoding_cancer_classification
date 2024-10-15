package com.dicoding.asclepius.data.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CancerClassification(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "image")
    var image: String = "",

    @ColumnInfo(name = "prediction")
    var prediction: String = "",

    @ColumnInfo(name = "confidence")
    var confidence: Float = 0.0f,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(prediction)
        parcel.writeFloat(confidence)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CancerClassification> {
        override fun createFromParcel(parcel: Parcel): CancerClassification {
            return CancerClassification(parcel)
        }

        override fun newArray(size: Int): Array<CancerClassification?> {
            return arrayOfNulls(size)
        }
    }
}