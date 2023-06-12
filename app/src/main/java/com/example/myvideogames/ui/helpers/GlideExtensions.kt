package com.example.myvideogames.ui.helpers

import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

fun RequestManager.loadImage(url: String?, isPreloading: Boolean): RequestBuilder<Bitmap> {

    val options = RequestOptions
        .diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)
        .dontAnimate()
        .signature(ObjectKey(url.plus(if (isPreloading) "_preloading" else "_not_preloading")))

    return asBitmap()
        .apply(options)
        .load(url)
}