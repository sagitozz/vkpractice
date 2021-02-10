package com.vkpractice.app.ui.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.children
import kotlin.math.max

/**
 * @autor d.snytko
 */
class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0
        var currentRowWidth = 0
        var currentRowHeight = 0

        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, height)
            currentRowWidth += child.measuredWidth
            if (currentRowWidth > desiredWidth) {
                currentRowWidth = child.measuredWidth
                currentRowHeight = child.measuredHeight
                height += child.measuredHeight
            } else {
                val tempRowHeight = max(currentRowHeight, child.measuredHeight)
                if (tempRowHeight > currentRowHeight) {
                    height += tempRowHeight - currentRowHeight
                    currentRowHeight = tempRowHeight
                }
            }
        }
        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = l + paddingLeft
        var currentTop = t + paddingTop
        var maxRowHeight = 0

        children.forEach { child ->
            var currentRight = currentLeft + child.measuredWidth
            if (child.measuredHeight > maxRowHeight && currentRight < measuredWidth)
                maxRowHeight = child.measuredHeight
            if (currentRight >= measuredWidth) {
                currentLeft = l + paddingLeft
                currentTop += maxRowHeight
                currentRight = currentLeft + child.measuredWidth
                maxRowHeight = child.measuredHeight
            }
            child.layout(currentLeft, currentTop, currentRight, currentTop + child.measuredHeight)
            currentLeft += child.measuredWidth
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}

