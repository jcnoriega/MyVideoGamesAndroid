package com.example.myvideogames.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Game(
    val id: String,
    val name: String,
    val backgroundImage: String
) : Parcelable