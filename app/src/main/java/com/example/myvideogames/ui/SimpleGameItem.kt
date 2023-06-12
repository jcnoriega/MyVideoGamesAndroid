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
abstract class SimpleGameItem: EpoxyModelWithHolder<SimpleGameHolder>() {

    //Annotation didn't work
    override fun getDefaultLayout() = R.layout.simple_item

    @EpoxyAttribute
    var imageUrl: String? = null
    @EpoxyAttribute
    lateinit var name: String
    @EpoxyAttribute
    lateinit var onClick: OnClickListener

    override fun bind(holder: SimpleGameHolder) {
        holder.nameView.text = name
        imageUrl?.let { holder.glide.load(it).into(holder.imageView) }
        holder.root.setOnClickListener(onClick)
    }
}

@EpoxyModelClass
abstract class GridSimpleItem: EpoxyModelWithHolder<SimpleGameHolder>()  {
    //Annotation didn't work
    override fun getDefaultLayout() = R.layout.grid_item

    @EpoxyAttribute
    var imageUrl: String? = null
    @EpoxyAttribute
    lateinit var name: String
    @EpoxyAttribute
    lateinit var onClick: OnClickListener

    override fun bind(holder: SimpleGameHolder) {
        holder.nameView.text = name
        imageUrl?.let { holder.glide.load(it).into(holder.imageView) }
        holder.root.setOnClickListener(onClick)
    }
}

@EpoxyModelClass
abstract class ShimmerSimpleItem: EpoxyModel<Any>()  {
    override fun getDefaultLayout() = R.layout.simple_item_shimmer
}

class SimpleGameHolder(parent: ViewParent) : EpoxyHolder(), Preloadable {

    lateinit var nameView: TextView
    lateinit var imageView: ShapeableImageView
    lateinit var root: ViewGroup
    val glide = Glide.with((parent as View).context)

    override fun bindView(itemView: View) {
        nameView = itemView.findViewById(R.id.name)
        imageView = itemView.findViewById(R.id.thumbnail)
        root = itemView.findViewById(R.id.simple_item_layout)
    }

    override val viewsToPreload by lazy { listOf(imageView) }
}
