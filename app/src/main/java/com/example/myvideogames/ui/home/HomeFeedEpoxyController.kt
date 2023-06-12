package com.example.myvideogames.ui.home

import com.airbnb.epoxy.TypedEpoxyController
import com.example.myvideogames.data.model.Game
import com.example.myvideogames.ui.gridSimpleItem
import com.example.myvideogames.ui.helpers.carouselBuilder
import com.example.myvideogames.ui.helpers.gridCarouselBuilder
import com.example.myvideogames.ui.listHeader
import com.example.myvideogames.ui.shimmerListHeader
import com.example.myvideogames.ui.shimmerSimpleItem
import com.example.myvideogames.ui.simpleGameItem

class HomeFeedEpoxyController : TypedEpoxyController<UiHomeFeed?>() {

    var onGameSelected: ((Game) -> Unit)? = null

    override fun buildModels(homeFeed: UiHomeFeed?) {
        homeFeed?.let { feed ->
            feed.topGames?.let {
                listHeader {
                    id("top_games_header")
                    title("Top Games")
                }
                carouselBuilder {
                    id("carousel_top")
                    it.forEach { game ->
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
            feed.latest?.let {
                listHeader {
                    id("top_games_header")
                    title("Latest Releases")
                }
                gridCarouselBuilder {
                    id("carousel_latest")
                    it.forEach { game ->
                        gridSimpleItem {
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

            shimmerListHeader {
                id("shimmer_list_header2")
            }
            carouselBuilder {
                id("carousel_shimmer2")
                repeat(5) {
                    shimmerSimpleItem {
                        id("2$it")
                    }
                }
            }
        }
    }
}