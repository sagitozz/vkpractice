package com.vkpractice.app.ui.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.vkpractice.app.R
import kotlinx.android.synthetic.main.post_view_text_only.view.*
import kotlinx.android.synthetic.main.post_view_text_only.view.groupAvatar
import kotlinx.android.synthetic.main.post_view_text_only.view.groupName
import kotlinx.android.synthetic.main.post_view_text_only.view.likeBtn
import kotlinx.android.synthetic.main.post_view_text_only.view.postDate
import kotlinx.android.synthetic.main.post_view_text_only.view.postText
import kotlinx.android.synthetic.main.post_view_text_only.view.titleBackGround
import kotlinx.android.synthetic.main.post_view_text_only.view.viewCount
import kotlinx.android.synthetic.main.post_view_text_only.view.viewsCountIcon


/**
 * @autor d.snytko
 */
class PostTextLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.post_view_text_only, this, true)
        viewsCountIcon.setImageResource(R.drawable.ic_eye)
        likeBtn.setImageResource(R.drawable.ic_like_gray)
        commentsBtn.setImageResource(R.drawable.ic_comments)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0

        measureChildWithMargins(groupAvatar, widthMeasureSpec, 0, heightMeasureSpec, height)
        height += groupAvatar.measuredHeight

        measureChildWithMargins(groupName, widthMeasureSpec, groupAvatar.measuredWidth + groupAvatar.marginStart + groupAvatar.marginEnd, heightMeasureSpec, height)
        height += groupName.measuredHeight

        measureChildWithMargins(postDate, widthMeasureSpec, 0, heightMeasureSpec, groupName.measuredHeight + groupName.marginTop + groupName.marginBottom)
        if (groupName.lineCount < 2) {
            height += postDate.measuredHeight
        }

        measureChildWithMargins(postText, widthMeasureSpec, 0, heightMeasureSpec, height)
        height += postText.measuredHeight

        measureChildWithMargins(viewCount, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(viewsCountIcon, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(likeBtn, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(likesCount, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(commentsBtn, widthMeasureSpec, 0, heightMeasureSpec, height)
        measureChildWithMargins(commentsCount, widthMeasureSpec, 0, heightMeasureSpec, height)

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
            currentLeft + groupName.measuredWidth + groupName.marginStart + groupName.marginEnd,
            currentTop + groupName.measuredHeight + groupName.marginBottom + groupName.marginTop
        )

        currentTop = groupName.measuredHeight + groupName.marginBottom

        postDate.layout(
            currentLeft + postDate.marginStart,
            currentTop,
            currentLeft + postDate.measuredWidth + postDate.marginStart,
            currentTop + postDate.measuredHeight
        )

        currentTop = titleBackGround.height + titleBackGround.marginBottom

        postText.layout(
            l + postText.marginStart,
            currentTop,
            postText.measuredWidth + postText.marginEnd,
            currentTop + postText.measuredHeight
        )

        currentTop += postText.measuredHeight + postText.marginEnd

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
                (commentsBtn.marginStart + commentsBtn.measuredWidth + commentsBtn.marginEnd) -
                (likesCount.marginStart + likesCount.measuredWidth + likesCount.marginEnd) -
                (commentsCount.marginStart + commentsCount.measuredWidth + commentsCount.marginEnd)

        likeBtn.layout(
            currentLeft,
            currentTop,
            currentLeft + likeBtn.measuredWidth,
            currentTop + likeBtn.measuredHeight
        )

        currentLeft += likeBtn.marginStart + likeBtn.measuredWidth + likeBtn.marginEnd

        likesCount.layout(
            currentLeft,
            currentTop,
            currentLeft + likesCount.measuredWidth + likesCount.marginStart,
            currentTop + likesCount.measuredHeight
        )

        currentLeft += likesCount.marginStart + likesCount.measuredWidth + likesCount.marginEnd

        commentsBtn.layout(
            currentLeft,
            currentTop,
            currentLeft + commentsBtn.measuredWidth,
            currentTop + commentsBtn.measuredHeight
        )

        currentLeft += commentsBtn.marginStart + commentsBtn.measuredWidth + commentsBtn.marginEnd

        commentsCount.layout(
            currentLeft,
            currentTop,
            currentLeft + commentsCount.measuredWidth + commentsCount.marginStart,
            currentTop + commentsCount.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}

