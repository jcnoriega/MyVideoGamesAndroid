package com.example.myvideogames.ui.home

import com.airbnb.epoxy.TypedEpoxyController
import com.example.myvideogames.data.Game
import com.example.myvideogames.header
import com.example.myvideogames.ui.helpers.carouselBuilder
import com.example.myvideogames.ui.simpleGameItem

class HomeFeedEpoxyController: TypedEpoxyController<List<Game>>() {

    var onGameSelected: ((Game) -> Unit) ? = null

    override fun buildModels(data: List<Game>) {
        header {
            id("header_latest")
            title("Latest Releases")
        }
        carouselBuilder {
            id("carousel_latest")
            data.forEach { game ->
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