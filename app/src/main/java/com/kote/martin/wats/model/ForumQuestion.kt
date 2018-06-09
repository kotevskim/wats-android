package com.kote.martin.wats.model

import android.os.Parcel
import android.os.Parcelable

data class ForumQuestion(val id: Long,
                         val description: String,
                         val datePublished: String,
                         val user: User
) : Item, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(User::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(description)
        parcel.writeString(datePublished)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForumQuestion> {
        override fun createFromParcel(parcel: Parcel): ForumQuestion {
            return ForumQuestion(parcel)
        }

        override fun newArray(size: Int): Array<ForumQuestion?> {
            return arrayOfNulls(size)
        }
    }
}