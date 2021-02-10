package com.vkpractice.app.ui.newsFragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.ui.layouts.PostTextLayout
import com.vkpractice.app.ui.layouts.PostWithImageLayout
import com.vkpractice.app.ui.recycler.ItemTouchHelperAdapter
import com.vkpractice.app.ui.recycler.PostsDiffUtilsCallback
import com.vkpractice.app.ui.recycler.itemdecoration.DateCallbackAdapter
import com.vkpractice.app.utils.DateHelper
import com.vkpractice.app.utils.ImageLoader
import com.vkpractice.app.utils.extensions.toBoolean
import kotlinx.android.synthetic.main.post_view_text_only.view.*
import kotlinx.android.synthetic.main.post_view_with_image.view.*
import kotlinx.android.synthetic.main.post_view_with_image.view.groupAvatar
import kotlinx.android.synthetic.main.post_view_with_image.view.groupName
import kotlinx.android.synthetic.main.post_view_with_image.view.likeBtn
import kotlinx.android.synthetic.main.post_view_with_image.view.postDate
import kotlinx.android.synthetic.main.post_view_with_image.view.postImage
import kotlinx.android.synthetic.main.post_view_with_image.view.postText
import kotlinx.android.synthetic.main.post_view_with_image.view.viewCount

/**
 * @autor d.snytko
 */
interface PostDelegate {
    fun onItemClick(postData: PostModel)
    fun onClickShare(post: PostModel)
    fun onImageClick(post: PostModel): Boolean
    fun onItemRemove(post: PostModel)
    fun onLikeItem(post: PostModel)
}

class PostAdapter(private val delegate: PostDelegate) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperAdapter, DateCallbackAdapter {

    private val differ = AsyncListDiffer(this, PostsDiffUtilsCallback())

    var newsList: List<PostModel> = listOf()
        set(value) {
            differ.submitList(value)
            field = value
        }
        get() = differ.currentList

    fun update(dataList: List<PostModel>) {
        newsList = dataList.toMutableList()
    }

    inner class PostWithImageVH(itemView: View, private val delegate: PostDelegate) :
        RecyclerView.ViewHolder(itemView) {

        init {
            with(itemView) {
                shareBtn.setOnClickListener {
                    delegate.onClickShare(newsList[bindingAdapterPosition])
                }
                setOnClickListener {
                    delegate.onItemClick(newsList[bindingAdapterPosition])
                }
                commentsBtns.setOnClickListener {
                    delegate.onItemClick(newsList[bindingAdapterPosition])
                }
                postImage.setOnLongClickListener {
                    delegate.onImageClick(newsList[bindingAdapterPosition])
                }
                postImage.setOnClickListener {
                    delegate.onItemClick(newsList[bindingAdapterPosition])
                }
                likeBtn.setOnClickListener {
                    delegate.onLikeItem(newsList[bindingAdapterPosition])
                }
            }
        }

        fun bind(item: PostModel) {
            with(itemView) {
                postDate.text = DateHelper.convertLongToTime(item.postDate)
                postText.text = item.postText
                postText.text = item.postText
                if (item.groupName == null) {
                    groupName.text = context.getString(R.string.commenter_name, item.firstName, item.lastName)
                } else groupName.text = item.groupName
                viewCount.text = item.postViews.toString()
                likesCounts.text = item.postLikes.toString()
                commentsCounts.text = item.comments.toString()
                ImageLoader.loadAvatarImage(item.groupAvatar, groupAvatar)
                ImageLoader.loadImage(item.postImage, postImage)
                if (item.userLikes.toBoolean()) {
                    likeBtn.setImageResource(R.drawable.ic_like_red)
                } else {
                    likeBtn.setImageResource(R.drawable.ic_like_gray)
                }
            }
        }
    }

    inner class PostTextVH(itemView: View, private val delegate: PostDelegate?) :
        RecyclerView.ViewHolder(itemView) {

        init {
            with(itemView) {
                setOnClickListener {
                    delegate?.onItemClick(newsList[bindingAdapterPosition])
                }
                likeBtn.setOnClickListener {
                    delegate?.onLikeItem(newsList[bindingAdapterPosition])
                }
                commentsBtn.setOnClickListener {
                    delegate?.onItemClick(newsList[bindingAdapterPosition])
                }
            }
        }

        fun bind(item: PostModel) {
            with(itemView) {
                postDate.text = DateHelper.convertLongToTime(item.postDate)
                postText.text = item.postText
                if (item.groupName == null) {
                    groupName.text = context.getString(R.string.commenter_name, item.firstName, item.lastName)
                } else groupName.text = item.groupName
                viewCount.text = item.postViews.toString()
                likesCount.text = item.postLikes.toString()
                commentsCount.text = item.comments.toString()
                ImageLoader.loadAvatarImage(item.groupAvatar, groupAvatar)
                if (item.userLikes.toBoolean()) {
                    likeBtn.setImageResource(R.drawable.ic_like_red)
                } else likeBtn.setImageResource(R.drawable.ic_like_gray)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == POST_WITH_IMAGE)
            return PostWithImageVH(PostWithImageLayout(parent.context), delegate)
        else (return PostTextVH(PostTextLayout(parent.context), delegate))
    }

    override fun getItemViewType(position: Int): Int {
        return if (newsList[position].postImage.isEmpty()) {
            TEXT_POST
        } else {
            POST_WITH_IMAGE
        }
    }

    override fun getItemCount() = newsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == POST_WITH_IMAGE) {
            (holder as PostWithImageVH).bind(newsList[position])
        } else {
            (holder as PostTextVH).bind(newsList[position])
        }
    }

    override fun onItemDismiss(position: Int) {
        delegate.onItemRemove(newsList[position])
    }

    override fun onItemLike(position: Int, viewHolder: RecyclerView.ViewHolder) {
        if (newsList[position].userLikes.toBoolean()) {
            viewHolder.itemView.likeBtn.setImageResource(R.drawable.ic_like_red)
        } else {
            viewHolder.itemView.likeBtn.setImageResource(R.drawable.ic_like_gray)
        }
        delegate.onLikeItem(newsList[position])
    }

    override fun elementHasDateHeader(position: Int): Boolean {
        return position == 0 || DateHelper.isSameDays(
            newsList[position].postDate,
            newsList[position - 1].postDate
        )
    }

    override fun getDateHeaderText(position: Int): String = DateHelper.setDateInDecorator(position, newsList)

    private companion object {

        const val POST_WITH_IMAGE = 0
        const val TEXT_POST = 1
    }
}
