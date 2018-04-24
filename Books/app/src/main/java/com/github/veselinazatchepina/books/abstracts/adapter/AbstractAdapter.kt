package com.github.veselinazatchepina.books.abstracts.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.books.utils.inflate


abstract class AbstractAdapter<ITEM> constructor(
        protected var items: List<ITEM>,
        private val layoutResId: Int,
        private val emptyLayoutResId: Int) : RecyclerView.Adapter<AbstractAdapter.Holder>() {

    companion object {
        private const val EMPTY_LIST = 0
        private const val NOT_EMPTY_LIST = 1
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount() = if (items.isEmpty()) {
        1
    } else {
        items.size
    }

    override fun getItemViewType(position: Int) = if (items.isEmpty()) EMPTY_LIST else NOT_EMPTY_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var viewHolder: Holder? = null
        when (viewType) {
            NOT_EMPTY_LIST -> {
                val view = parent inflate layoutResId
                viewHolder = Holder(view)
                val itemView = viewHolder.itemView
                defineClick(viewHolder, itemView)
                defineLongClick(viewHolder, itemView)
            }
            EMPTY_LIST -> {
                val view = parent inflate emptyLayoutResId
                viewHolder = Holder(view)
            }
        }

        return viewHolder!!
    }

    private fun defineClick(viewHolder: Holder?, itemView: View) {
        itemView.setOnClickListener {
            val adapterPosition = viewHolder!!.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(viewHolder.itemView, adapterPosition)
            }
        }
    }

    private fun defineLongClick(viewHolder: Holder?, itemView: View) {
        itemView.setOnLongClickListener {
            val adapterPosition = viewHolder!!.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongItemClick(itemView, adapterPosition)
            }
            true
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    protected open fun onItemClick(itemView: View, position: Int) {

    }

    protected open fun onLongItemClick(itemView: View, position: Int) {

    }

    fun update(newItems: List<ITEM>) {
        items = newItems
        notifyDataSetChanged()
    }
}