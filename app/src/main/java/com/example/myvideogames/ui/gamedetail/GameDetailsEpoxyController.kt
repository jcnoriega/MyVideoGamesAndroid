package com.example.myvideogames.ui.gamedetail

import com.airbnb.epoxy.TypedEpoxyController
import com.example.myvideogames.data.model.Game
import com.example.myvideogames.data.model.GameTrailer
import com.example.myvideogames.ui.helpers.carouselBuilder
import com.example.myvideogames.ui.listHeader
import com.example.myvideogames.ui.shimmerListHeader
import com.example.myvideogames.ui.shimmerSimpleItem
import com.example.myvideogames.ui.simpleGameItem

class GameDetailsEpoxyController: TypedEpoxyController<UiGameDetails?>() {

    var onTrailerSelected: ((GameTrailer) -> Unit) ? = null
    var onAdditionSelected: ((Game) -> Unit) ? = null

    override fun buildModels(data: UiGameDetails?) {
        data?.let { details ->
            gameDescription {
                id("game_description")
                description(data.description.description)
                isExpanded(data.description.isExpanded)
                onClick(this@GameDetailsEpoxyController::descriptionClicked)
            }
            if (!details.trailers.isNullOrEmpty()) {
                listHeader {
                    id("trailers_header")
                    title("Trailers")
                }
                carouselBuilder {
                    id("carousel_trailers")
                    details.trailers.forEach { trailer ->
                        simpleGameItem {
                            id("game_trailer{${trailer.id}}")
                            name(trailer.name)
                            imageUrl(trailer.preview)
                            onClick { _ ->
                                this@GameDetailsEpoxyController.onTrailerSelected?.let { it(trailer) }
                            }
                        }
                    }
                }
            }
            if (!details.additions.isNullOrEmpty()) {
                listHeader {
                    id("additions")
                    title("More")
                }
                carouselBuilder {
                    id("carousel_addition")
                    details.additions.forEach { game ->
                        simpleGameItem {
                            id("game_addition{${game.id}}")
                            name(game.name)
                            imageUrl(game.backgroundImage)
                            onClick { _ ->
                                this@GameDetailsEpoxyController.onAdditionSelected?.let { it(game) }
                            }
                        }
                    }
                }
            }
        } ?: run {
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
        }
    }

    private fun descriptionClicked() {
        currentData?.description?.isExpanded?.let {
            currentData?.description?.isExpanded = !it
            setData(currentData)
        }
    }
}