package com.vkpractice.app.ui.detailsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.ui.recycler.ItemTouchHelperAdapter
import com.vkpractice.app.ui.recycler.PostsDiffUtilsCallback
import com.vkpractice.app.utils.DateHelper
import com.vkpractice.app.utils.ImageLoader
import com.vkpractice.app.utils.extensions.toBoolean
import kotlinx.android.synthetic.main.details_post_item.view.*


/**
 * @autor d.snytko
 */
interface DetailsPostDelegate {
    fun onClickShare(post: PostModel)
    fun onPressLike(post: PostModel)
    fun onImageClick(post: PostModel): Boolean
}

class DetailPostAdapter(private val delegate: DetailsPostDelegate) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    private val differ = AsyncListDiffer(this, PostsDiffUtilsCallback())

    private var newsList: List<PostModel> = listOf()
        set(value) {
            differ.submitList(value)
            field = value
        }
        get() = differ.currentList

    fun update(dataList: List<PostModel>) {
        newsList = dataList.toMutableList()
    }

    inner class DetailPostVH(itemView: View, private val delegate: DetailsPostDelegate) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.likeBtnDetails.setOnClickListener { delegate.onPressLike(newsList[absoluteAdapterPosition]) }
            itemView.shareBtnDetails.setOnClickListener { delegate.onClickShare(newsList[absoluteAdapterPosition]) }
            itemView.postImageDetails.setOnLongClickListener { delegate.onImageClick(newsList[absoluteAdapterPosition]) }
        }

        fun bind(item: PostModel) {
            with(itemView) {
                if (item.groupName == null) {
                    groupNameDetails.text = itemView.context.getString(R.string.commenter_name, item.firstName, item.lastName)
                } else groupNameDetails.text = item.groupName
                postDateDetails.text = DateHelper.convertLongToDate(item.postDate)
                ImageLoader.loadAvatarImage(item.groupAvatar, groupAvatarDetails)
                if (item.postImage != "") {
                    ImageLoader.loadImage(item.postImage, postImageDetails)
                }
                postTextDetails.text = item.postText
                likesCountsDetails.text = item.postLikes.toString()
                viewCountDetails.text = item.postViews.toString()
                if (item.userLikes.toBoolean()) {
                    likeBtnDetails.setImageResource(R.drawable.ic_like_red)
                } else {
                    likeBtnDetails.setImageResource(R.drawable.ic_like_gray)
                }
            }
        }
    }

    override fun onItemDismiss(position: Int) {
        delegate.onPressLike(newsList[position])
    }

    override fun onItemLike(position: Int, viewHolder: RecyclerView.ViewHolder) {
        delegate.onPressLike(newsList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DetailPostVH(LayoutInflater.from(parent.context).inflate(R.layout.details_post_item, parent, false), delegate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DetailPostVH).bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size
}
