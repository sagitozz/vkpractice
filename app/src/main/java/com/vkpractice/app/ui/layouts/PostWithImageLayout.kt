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
import kotlinx.android.synthetic.main.post_view_with_image.view.*

/**
 * @autor d.snytko
 */
class PostWithImageLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        inflate(context, R.layout.post_view_with_image, this)
        viewsCountIcon.setImageResource(R.drawable.ic_eye)
        likeBtn.setImageResource(R.drawable.ic_like_gray)
        shareBtn.setImageResource(R.drawable.ic_share)
        commentsBtns.setImageResource(R.drawable.ic_comments)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0

        measureChildWithMargins(groupAvatar, widthMeasureSpec, 0, heightMeasureSpec, height)
        height += groupAvatar.measuredHeight

        measureChildWithMargins(
            groupName,
            widthMeasureSpec,
            groupAvatar.measuredWidth + groupAvatar.marginStart + groupAvatar.marginEnd,
            heightMeasureSpec,
            height
        )
        height += groupName.measuredHeight

        measureChildWithMargins(
            postDate,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            groupName.measuredHeight + groupName.marginTop + groupName.marginBottom
        )
        if (groupName.lineCount < 2) {
            height += postDate.measuredHeight
        }

        if (postText.text != "") {
            measureChildWithMargins(postText, widthMeasureSpec, 0, heightMeasureSpec, height)
            height += postText.measuredHeight
        }

        measureChildWithMargins(postImage, widthMeasureSpec, 0, heightMeasureSpec, height)
        height +=
            postImage.measuredHeight + postImage.marginBottom
        measureChildWithMargins(viewsCountIcon, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(viewCount, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(likeBtn, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(likesCounts, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(commentsBtns, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(shareBtn, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(commentsCounts, widthMeasureSpec, 0, heightMeasureSpec, height)
        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = 0 + paddingLeft
        var currentTop = 0 + paddingTop

        titleBackGround.layout(
            currentLeft,
            currentTop,
            r,
            groupAvatar.measuredHeight + groupAvatar.marginBottom
        )

        groupAvatar.layout(
            currentLeft + groupAvatar.marginStart,
            currentTop,
            currentLeft + groupAvatar.measuredWidth,
            currentTop + groupAvatar.measuredHeight + groupAvatar.marginBottom
        )

        currentLeft += groupAvatar.measuredWidth

        groupName.layout(
            currentLeft + groupName.marginStart,
            currentTop + groupName.marginTop,
            currentLeft + groupName.measuredWidth + groupName.marginStart,
            currentTop + groupName.measuredHeight + groupName.marginEnd + groupName.marginTop
        )

        currentTop = groupName.measuredHeight + groupName.marginBottom

        postDate.layout(
            currentLeft + postDate.marginStart,
            currentTop,
            currentLeft + postDate.measuredWidth + postDate.marginStart,
            currentTop + postDate.measuredHeight
        )

        currentTop = titleBackGround.height + titleBackGround.marginBottom

        if (postText.text != "") {
            postText.layout(
                l + postText.marginStart,
                currentTop,
                postText.measuredWidth + postText.marginEnd,
                currentTop + postText.measuredHeight
            )
            currentTop += postText.measuredHeight + postText.marginBottom
        }

        postImage.layout(
            l,
            currentTop,
            r,
            currentTop + postImage.measuredHeight
        )

        currentTop += postImage.measuredHeight + postImage.marginBottom
        currentLeft = 0

        viewsCountIcon.layout(
            currentLeft + viewsCountIcon.marginStart,
            currentTop,
            currentLeft + viewsCountIcon.measuredWidth + viewsCountIcon.marginStart,
            currentTop + viewsCountIcon.measuredHeight
        )

        currentLeft += viewsCountIcon.marginStart + viewsCountIcon.measuredWidth + viewCount.marginEnd

        viewCount.layout(
            currentLeft,
            currentTop,
            currentLeft + viewCount.measuredWidth + viewCount.marginStart,
            currentTop + viewCount.measuredHeight
        )

        currentLeft = 0
        currentLeft += right -
                (likeBtn.marginStart + likeBtn.measuredWidth + likeBtn.marginEnd) -
                (commentsBtns.marginStart + commentsBtns.measuredWidth + commentsBtns.marginEnd) -
                (likesCounts.marginStart + likesCounts.measuredWidth + likesCounts.marginEnd) -
                (commentsCounts.marginStart + commentsCounts.measuredWidth + commentsCounts.marginEnd) -
                (shareBtn.marginStart + shareBtn.measuredWidth + shareBtn.marginEnd)


        likeBtn.layout(
            currentLeft,
            currentTop,
            currentLeft + likeBtn.measuredWidth,
            currentTop + likeBtn.measuredHeight
        )

        currentLeft += likeBtn.marginStart + likeBtn.measuredWidth + likeBtn.marginEnd

        likesCounts.layout(
            currentLeft,
            currentTop,
            currentLeft + likesCounts.measuredWidth + likesCounts.marginStart,
            currentTop + likesCounts.measuredHeight
        )

        currentLeft += likesCounts.marginStart + likesCounts.measuredWidth + likesCounts.marginEnd

        commentsBtns.layout(
            currentLeft,
            currentTop,
            currentLeft + commentsBtns.measuredWidth,
            currentTop + commentsBtns.measuredHeight
        )

        currentLeft += commentsBtns.marginStart + commentsBtns.measuredWidth + commentsBtns.marginEnd

        commentsCounts.layout(
            currentLeft,
            currentTop,
            currentLeft + commentsCounts.measuredWidth,
            currentTop + commentsCounts.measuredHeight
        )

        currentLeft += commentsCounts.marginStart + commentsCounts.measuredWidth + commentsCounts.marginEnd

        shareBtn.layout(
            currentLeft,
            currentTop,
            currentLeft + shareBtn.measuredWidth,
            currentTop + shareBtn.measuredHeight
        )
    }


    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}

