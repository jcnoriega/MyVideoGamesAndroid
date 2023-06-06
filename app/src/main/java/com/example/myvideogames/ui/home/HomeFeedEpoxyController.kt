package com.example.myvideogames.ui.home

import com.airbnb.epoxy.EpoxyController
import com.example.myvideogames.data.Game
import com.example.myvideogames.header
import com.example.myvideogames.ui.helpers.carouselBuilder
import com.example.myvideogames.ui.simpleGameItem

class HomeFeedEpoxyController: EpoxyController() {

    var games: List<Game> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    var onGameSelected: ((Game) -> Unit) ? = null

    override fun buildModels() {
        header {
            id("header_latest")
            title("Latest Releases")
        }
        carouselBuilder {
            id("carousel_latest")
            this@HomeFeedEpoxyController.games.forEach { game ->
                simpleGameItem {
                    id("game{${game.id}}")
                    name(game.name)
                    imageUrl(game.backgroundImage)
                    onClick { _ ->
                        this@HomeFeedEpoxyController.onGameSelected?.let { it(game) }
                    }
                }
            }
        }
    }
}