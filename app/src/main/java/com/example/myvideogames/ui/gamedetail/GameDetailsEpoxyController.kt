package com.example.myvideogames.ui.gamedetail

import com.airbnb.epoxy.TypedEpoxyController
import com.example.myvideogames.data.model.GameTrailer
import com.example.myvideogames.ui.helpers.carouselBuilder
import com.example.myvideogames.ui.simpleGameItem

class GameDetailsEpoxyController: TypedEpoxyController<List<GameTrailer>>() {

    var onGameSelected: ((GameTrailer) -> Unit) ? = null

    override fun buildModels(data: List<GameTrailer>) {
//        header {
//            id("header_trailers")
//            title("Trailers")
//        }
        if (data.isEmpty()) {
            return
        }
        carouselBuilder {
            id("carousel_trailers")
            data.forEach { trailer ->
                simpleGameItem {
                    id("game_trailer{${trailer.id}}")
                    name(trailer.name)
                    imageUrl(trailer.preview)
                    onClick { _ ->
                        this@GameDetailsEpoxyController.onGameSelected?.let { it(trailer) }
                    }
                }
            }
        }
    }
}