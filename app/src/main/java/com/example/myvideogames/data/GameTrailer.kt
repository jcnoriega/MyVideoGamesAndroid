package com.example.myvideogames.data

import com.google.gson.annotations.SerializedName

data class GameTrailer(
    val id: Int,
    val name: String,
    val preview: String,
    @SerializedName("480")
    val small: String? = null,
    val max: String? = null
)
