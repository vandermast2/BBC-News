package com.breez.testapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breez.testapplication.R
import com.breez.testapplication.dataFlow.network.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class MyViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(article: Article?, listener: Listener) {
        if (article != null) {
            with(article) {
                txtTitle.text = title
                txtDescription.text = description
                txtContent.text = content
                Picasso.get().load(urlToImage)
                    .placeholder(R.drawable.placeholder).into(newsScreen)
                itemView.setOnClickListener { listener.onClick(this.url!!) }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
            return MyViewHolder(view)
        }
    }
}