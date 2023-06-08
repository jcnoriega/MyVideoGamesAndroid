package com.example.myvideogames.data.model

import com.google.gson.annotations.SerializedName

data class GameTrailer(
    val id: Int,
    val name: String,
    val preview: String,
    val data: GameTrailerData
) {
    data class GameTrailerData(
        @SerializedName("480")
        val small: String? = null,
        val max: String? = null
    )
}
