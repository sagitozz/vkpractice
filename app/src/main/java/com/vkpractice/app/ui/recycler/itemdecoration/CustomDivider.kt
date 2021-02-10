package com.vkpractice.app.ui.recycler.itemdecoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.utils.extensions.dpToPx

/**
 * @autor d.snytko
 */
class CustomDivider(
    private val dateHeaderView: TextView,
    private val callback: DateCallbackAdapter
) : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        layoutDateHeaderView(parent)

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val viewHolder = parent.getChildViewHolder(view)
            val relativePosition = getRelativePosition(viewHolder)

            if (relativePosition != -1) {
                if (callback.elementHasDateHeader(relativePosition)) {
                    dateHeaderView.text = callback.getDateHeaderText(relativePosition)
                    drawDateHeaderView(c, view)
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(view)
        val relativePosition = getRelativePosition(viewHolder)
        val offset = if (relativePosition == 0) {
            parent.context.dpToPx(FIRST_ELEMENT_OFFSET)
        } else {
            parent.context.dpToPx(OTHER_ELEMENT_OFFSET)
        }
        when {
            relativePosition == -1 -> return
            callback.elementHasDateHeader(relativePosition) -> {
                measureDateHeaderView(parent)
                initOffsetIfElementHasDateHeader(
                    outRect,
                    offset,
                    relativePosition,
                    parent.adapter!!.itemCount
                )
            }
            else -> {
                initOffset(outRect, offset, relativePosition, parent.adapter!!.itemCount)
            }
        }
    }

    private fun getRelativePosition(viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder.bindingAdapter is DateCallbackAdapter) {
            viewHolder.bindingAdapterPosition
        } else -1
    }

    private fun initOffset(outRect: Rect, offset: Int, position: Int, itemCount: Int) {
        when (position) {
            0 -> outRect.bottom = offset
            itemCount - 1 -> outRect.top - offset
            else -> {
                outRect.top = offset
                outRect.bottom = offset
            }
        }
    }

    private fun initOffsetIfElementHasDateHeader(
        outRect: Rect,
        offset: Int,
        position: Int,
        itemCount: Int
    ) {
        outRect.top = dateHeaderView.measuredHeight + offset
        if (position != itemCount - 1) {
            outRect.bottom = offset
        }
    }

    private fun measureDateHeaderView(parent: ViewGroup) {
        val width = parent.context.dpToPx(DATE_VIEW_WIDTH)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.AT_MOST)
        dateHeaderView.measure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun layoutDateHeaderView(parent: ViewGroup) {
        val left = parent.left + parent.measuredWidth / 2 - dateHeaderView.measuredWidth / 2
        val top = 0
        val right = left + dateHeaderView.measuredWidth
        val bottom = top + dateHeaderView.measuredHeight
        dateHeaderView.layout(left, top, right, bottom)
    }

    private fun drawDateHeaderView(c: Canvas, view: View) {
        val offset = view.context.dpToPx(MARGIN)
        val dx = dateHeaderView.left.toFloat()
        val dy = (view.top - offset - dateHeaderView.measuredHeight).toFloat()

        c.save()
        c.translate(dx, dy)
        dateHeaderView.draw(c)
        c.restore()
    }

    private companion object {
        const val MARGIN = 8
        const val DATE_VIEW_WIDTH = 160
        const val FIRST_ELEMENT_OFFSET = 16
        const val OTHER_ELEMENT_OFFSET = 8
    }
}
