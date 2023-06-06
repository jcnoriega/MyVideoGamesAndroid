package com.example.myvideogames.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val id: String,
    val name: String,
    val backgroundImage: String
) : Parcelable