package com.vkpractice.app.ui.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.vkpractice.app.R
import kotlinx.android.synthetic.main.post_details_layout.view.groupAvatarDetails
import kotlinx.android.synthetic.main.post_details_layout.view.groupNameDetails
import kotlinx.android.synthetic.main.post_details_layout.view.likeBtnDetails
import kotlinx.android.synthetic.main.post_details_layout.view.likesCountsDetails
import kotlinx.android.synthetic.main.post_details_layout.view.postDateDetails
import kotlinx.android.synthetic.main.post_details_layout.view.postImageDetails
import kotlinx.android.synthetic.main.post_details_layout.view.postTextDetails
import kotlinx.android.synthetic.main.post_details_layout.view.shareBtnDetails
import kotlinx.android.synthetic.main.post_details_layout.view.titleBackGroundDetails
import kotlinx.android.synthetic.main.post_details_layout.view.viewCountDetails
import kotlinx.android.synthetic.main.post_details_layout.view.viewsCountIconDetails

/**
 * @autor d.snytko
 */
class DetailsPostLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        inflate(context, R.layout.post_details_layout, this)
        viewsCountIconDetails.setImageResource(R.drawable.ic_eye)
        likeBtnDetails.setImageResource(R.drawable.ic_like_gray)
        shareBtnDetails.setImageResource(R.drawable.ic_share)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0

        measureChildWithMargins(groupAvatarDetails, widthMeasureSpec, 0, heightMeasureSpec, height)
        height += groupAvatarDetails.measuredHeight

        measureChildWithMargins(
            groupNameDetails,
            widthMeasureSpec,
            groupAvatarDetails.measuredWidth + groupAvatarDetails.marginStart + groupAvatarDetails.marginEnd,
            heightMeasureSpec,
            height
        )
        height += groupNameDetails.measuredHeight

        measureChildWithMargins(
            postDateDetails,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            groupNameDetails.measuredHeight + groupNameDetails.marginTop + groupNameDetails.marginBottom
        )
        if (groupNameDetails.lineCount < 2) {
            height += postDateDetails.measuredHeight
        }

        if (postTextDetails.text != "") {
            measureChildWithMargins(postTextDetails, widthMeasureSpec, 0, heightMeasureSpec, height)
            height += postTextDetails.measuredHeight
        }
        
        measureChildWithMargins(postImageDetails, widthMeasureSpec, 0, heightMeasureSpec, height)
        height +=
            postImageDetails.measuredHeight + postImageDetails.marginBottom
        measureChildWithMargins(
            viewsCountIconDetails,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )
        measureChildWithMargins(viewCountDetails, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(likeBtnDetails, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(likesCountsDetails, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(shareBtnDetails, widthMeasureSpec, 0, heightMeasureSpec, height)

        height += likeBtnDetails.marginBottom
        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = 0 + paddingLeft
        var currentTop = 0 + paddingTop

        titleBackGroundDetails.layout(
            currentLeft,
            currentTop,
            r,
            groupAvatarDetails.measuredHeight + groupAvatarDetails.marginBottom
        )

        groupAvatarDetails.layout(
            currentLeft + groupAvatarDetails.marginStart,
            currentTop,
            currentLeft + groupAvatarDetails.measuredWidth,
            currentTop + groupAvatarDetails.measuredHeight + groupAvatarDetails.marginBottom
        )

        currentLeft += groupAvatarDetails.measuredWidth

        groupNameDetails.layout(
            currentLeft + groupNameDetails.marginStart,
            currentTop + groupNameDetails.marginTop,
            currentLeft + groupNameDetails.measuredWidth + groupNameDetails.marginStart,
            currentTop + groupNameDetails.measuredHeight + groupNameDetails.marginEnd + groupNameDetails.marginTop
        )

        currentTop = groupNameDetails.measuredHeight + groupNameDetails.marginBottom

        postDateDetails.layout(
            currentLeft + postDateDetails.marginStart,
            currentTop,
            currentLeft + postDateDetails.measuredWidth + postDateDetails.marginStart,
            currentTop + postDateDetails.measuredHeight
        )

        currentTop = titleBackGroundDetails.height + titleBackGroundDetails.marginBottom

        if (postTextDetails.text != "") {
            postTextDetails.layout(
                l + postTextDetails.marginStart,
                currentTop,
                postTextDetails.measuredWidth + postTextDetails.marginEnd,
                currentTop + postTextDetails.measuredHeight
            )
            currentTop += postTextDetails.measuredHeight + postTextDetails.marginBottom
        }

        postImageDetails.layout(
            l,
            currentTop,
            r,
            currentTop + postImageDetails.measuredHeight
        )

        currentTop += postImageDetails.measuredHeight + postImageDetails.marginBottom
        currentLeft = 0

        viewsCountIconDetails.layout(
            currentLeft + viewsCountIconDetails.marginStart,
            currentTop,
            currentLeft + viewsCountIconDetails.measuredWidth + viewsCountIconDetails.marginStart,
            currentTop + viewsCountIconDetails.measuredHeight
        )

        currentLeft += viewsCountIconDetails.marginStart + viewsCountIconDetails.measuredWidth +
                viewCountDetails.marginEnd

        viewCountDetails.layout(
            currentLeft,
            currentTop,
            currentLeft + viewCountDetails.measuredWidth + viewCountDetails.marginStart,
            currentTop + viewCountDetails.measuredHeight
        )

        currentLeft = 0
        currentLeft += right -
                (likeBtnDetails.marginStart + likeBtnDetails.measuredWidth + likeBtnDetails.marginEnd) -
                (likesCountsDetails.marginStart + likesCountsDetails.measuredWidth + likesCountsDetails.marginEnd) -
                (shareBtnDetails.marginStart + shareBtnDetails.measuredWidth + shareBtnDetails.marginEnd)


        likeBtnDetails.layout(
            currentLeft,
            currentTop,
            currentLeft + likeBtnDetails.measuredWidth,
            currentTop + likeBtnDetails.measuredHeight
        )

        currentLeft += likeBtnDetails.marginStart + likeBtnDetails.measuredWidth + likeBtnDetails.marginEnd

        likesCountsDetails.layout(
            currentLeft,
            currentTop,
            currentLeft + likesCountsDetails.measuredWidth + likesCountsDetails.marginStart,
            currentTop + likesCountsDetails.measuredHeight
        )

        currentLeft += likesCountsDetails.marginStart + likesCountsDetails.measuredWidth + likesCountsDetails.marginEnd

        shareBtnDetails.layout(
            currentLeft,
            currentTop,
            currentLeft + shareBtnDetails.measuredWidth,
            currentTop + shareBtnDetails.measuredHeight
        )
    }


    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}

