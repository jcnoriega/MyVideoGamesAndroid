package com.example.myvideogames.ui.helpers

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.CarouselModelBuilder
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelCollector
import com.airbnb.epoxy.ModelView

/**
 * Example that illustrate how to add a builder for nested list (ex: carousel) that allow building
 * it using DSL :
 *
 *   carouselBuilder {
 *     id(...)
 *     for (...) {
 *       carouselItemCustomView {
 *         id(...)
 *       }
 *     }
 *   }
 *
 * @link https://github.com/airbnb/epoxy/issues/847
 */
fun ModelCollector.carouselBuilder(builder: EpoxyCarouselBuilder.() -> Unit): CarouselModel_ {
    Carousel.setDefaultGlobalSnapHelperFactory(null)
    val carouselBuilder = EpoxyCarouselBuilder().apply { builder() }
    add(carouselBuilder.carouselModel)
    return carouselBuilder.carouselModel
}

class EpoxyCarouselBuilder(
    internal val carouselModel: CarouselModel_ = CarouselModel_()
) : ModelCollector, CarouselModelBuilder by carouselModel {
    private val models = mutableListOf<EpoxyModel<*>>()

    override fun add(model: EpoxyModel<*>) {
        models.add(model)

        // Set models list every time a model is added so that it can run debug validations to
        // ensure it is still valid to mutate the carousel model.
        carouselModel.models(models)
    }
}

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GridCarousel(context: Context?) : Carousel(context) {
    override fun createLayoutManager(): LayoutManager {
        return GridLayoutManager(context, SPAN_COUNT, LinearLayoutManager.HORIZONTAL, false)
    }

    companion object {
        private const val SPAN_COUNT = 2
    }
}

fun ModelCollector.gridCarouselBuilder(builder: EpoxyGridCarouselBuilder.() -> Unit): GridCarouselModel_ {
    val gridCarouselBuilder = EpoxyGridCarouselBuilder().apply { builder() }
    add(gridCarouselBuilder.gridCarouselModel)
    return gridCarouselBuilder.gridCarouselModel
}

class EpoxyGridCarouselBuilder(
    internal val gridCarouselModel: GridCarouselModel_ = GridCarouselModel_()
) : ModelCollector, GridCarouselModelBuilder by gridCarouselModel {
    private val models = mutableListOf<EpoxyModel<*>>()

    override fun add(model: EpoxyModel<*>) {
        models.add(model)

        // Set models list every time a model is added so that it can run debug validations to
        // ensure it is still valid to mutate the carousel model.
        gridCarouselModel.models(models)
    }
}
