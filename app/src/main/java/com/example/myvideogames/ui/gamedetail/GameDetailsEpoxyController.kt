package com.example.myvideogames.ui.gamedetail

import com.airbnb.epoxy.TypedEpoxyController
import com.example.myvideogames.data.model.GameTrailer
import com.example.myvideogames.ui.helpers.carouselBuilder
import com.example.myvideogames.ui.listHeader
import com.example.myvideogames.ui.shimmerListHeader
import com.example.myvideogames.ui.shimmerSimpleItem
import com.example.myvideogames.ui.simpleGameItem

class GameDetailsEpoxyController: TypedEpoxyController<List<GameTrailer>>() {

    var onGameSelected: ((GameTrailer) -> Unit) ? = null

    override fun buildModels(data: List<GameTrailer>) {
        if (data.isEmpty()) {
            shimmerListHeader {
                id("shimmer_list_header1")
            }
            carouselBuilder {
                id("carousel_shimmer1")
                repeat(5) {
                    shimmerSimpleItem {
                        id("1$it")
                    }
                }
            }
        } else {
            listHeader {
                id("trailers_header")
                title("Trailers")
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
}