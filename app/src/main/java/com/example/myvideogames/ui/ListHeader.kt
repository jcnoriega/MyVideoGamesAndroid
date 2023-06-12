package com.example.myvideogames.ui

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.preload.Preloadable
import com.bumptech.glide.Glide
import com.example.myvideogames.R
import com.google.android.material.imageview.ShapeableImageView

@EpoxyModelClass
abstract class ListHeader: EpoxyModelWithHolder<HeaderHolder>() {

    //Annotation didn't work
    override fun getDefaultLayout() = R.layout.home_header

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: HeaderHolder) {
        holder.titleView.text = title
    }
}

@EpoxyModelClass
abstract class ShimmerListHeader: EpoxyModel<Any>() {
    override fun getDefaultLayout() = R.layout.shimmer_home_header
}

class HeaderHolder(parent: ViewParent) : EpoxyHolder() {

    lateinit var titleView: TextView

    override fun bindView(itemView: View) {
        titleView = itemView as TextView
    }
}
