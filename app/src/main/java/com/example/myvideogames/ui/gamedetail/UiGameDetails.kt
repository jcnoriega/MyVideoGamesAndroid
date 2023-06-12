package com.example.myvideogames.ui.gamedetail

import com.example.myvideogames.data.model.Game
import com.example.myvideogames.data.model.GameTrailer

data class UiGameDetails(
    val rating: Float,
    val description: UiGameDescription,
    val trailers: List<GameTrailer>?,
    val additions: List<Game>?
)