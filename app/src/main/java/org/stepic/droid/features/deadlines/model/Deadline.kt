package org.stepic.droid.features.deadlines.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Deadline(
        val section: Long,
        val deadline: Date
): Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(section)
        parcel.writeLong(deadline.time)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Deadline> {
        override fun createFromParcel(parcel: Parcel): Deadline = Deadline(parcel.readLong(), Date(parcel.readLong()))
        override fun newArray(size: Int): Array<Deadline?> = arrayOfNulls(size)
    }
}