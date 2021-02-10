package com.vkpractice.app.ui.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

/**
 * @autor d.snytko
 */
interface ItemTouchHelperAdapter {

    fun onItemDismiss(position: Int)

    fun onItemLike(position: Int, viewHolder: RecyclerView.ViewHolder)
}

class ItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT or RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            LEFT -> adapter.onItemDismiss(viewHolder.bindingAdapterPosition)

            RIGHT -> adapter.onItemLike(viewHolder.bindingAdapterPosition, viewHolder)
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = 0
        val swipeFlags = LEFT or RIGHT

        return if (viewHolder.bindingAdapter is ItemTouchHelperAdapter) makeMovementFlags(dragFlags, swipeFlags)
        else 0
    }
}


