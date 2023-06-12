package com.example.myvideogames.ui.gamedetail

import android.view.View
import android.view.ViewParent
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.myvideogames.R

@EpoxyModelClass
abstract class GameDescriptionModel : EpoxyModelWithHolder<GameDescriptionHolder>() {

    override fun getDefaultLayout() = R.layout.game_description

    @EpoxyAttribute
    lateinit var description: String

    @EpoxyAttribute
    open var isExpanded: Boolean = false // make it open because of epoxy bug

    @EpoxyAttribute
    lateinit var onClick: () -> Unit


    override fun bind(holder: GameDescriptionHolder) {
        holder.description.text = description
        holder.description.isVisible = isExpanded
        holder.layout.setOnClickListener { onClick() }
    }
}

class GameDescriptionHolder(view: ViewParent) : EpoxyHolder() {

    lateinit var description: TextView
    lateinit var layout: View

    override fun bindView(itemView: View) {
        layout = itemView
        description = itemView.findViewById(R.id.description)
    }

}