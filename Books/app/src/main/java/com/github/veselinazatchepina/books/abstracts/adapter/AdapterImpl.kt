package com.github.veselinazatchepina.books.abstracts.adapter

import android.view.View


class AdapterImpl<ITEM>(items: List<ITEM>,
                        layoutResId: Int,
                        emptyLayoutResId: Int,
                        private val bindHolder: View.(ITEM) -> Unit
) : AbstractAdapter<ITEM>(items, layoutResId, emptyLayoutResId) {

    private var itemClick: ITEM.() -> Unit = {}
    private var longItemClick: ITEM.() -> Unit = {}

    constructor(items: List<ITEM>,
                layoutResId: Int,
                emptyLayoutResId: Int,
                bindHolder: View.(ITEM) -> Unit,
                itemClick: ITEM.() -> Unit = {},
                longItemClick: ITEM.() -> Unit = {}) : this(items, layoutResId, emptyLayoutResId, bindHolder) {
        this.itemClick = itemClick
        this.longItemClick = longItemClick
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (items.isNotEmpty()) {
            holder.itemView.bindHolder(items[position])
        }
    }

    override fun onItemClick(itemView: View, position: Int) {
        if (items.isNotEmpty()) {
            items[position].itemClick()
        }
    }

    override fun onLongItemClick(itemView: View, position: Int) {
        if (items.isNotEmpty()) {
            items[position].longItemClick()
        }
    }
}