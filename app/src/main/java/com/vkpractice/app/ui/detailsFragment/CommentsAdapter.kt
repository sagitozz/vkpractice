package com.vkpractice.app.ui.detailsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.ui.recycler.CommentsDiffUtilsCallback
import com.vkpractice.app.utils.DateHelper
import com.vkpractice.app.utils.ImageLoader
import kotlinx.android.synthetic.main.comments_item.view.*
import kotlinx.android.synthetic.main.empty_comments_item.view.*


/**
 * @autor d.snytko
 */
class CommentsAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, CommentsDiffUtilsCallback())

    private var newsList: List<CommentsModel> = listOf()
        set(value) {
            differ.submitList(value)
            field = value
        }
        get() = differ.currentList

    fun update(dataList: List<CommentsModel>) {
        newsList = dataList.toMutableList()
    }

    private class CommentsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CommentsModel) {
            with(itemView) {
                commentText.text = item.text
                commenterName.text = context.getString(R.string.commenter_name, item.firstName, item.lastName)
                ImageLoader.loadAvatarImage(item.avatar, commenterAvatar)
                commentDate.text =
                    if (DateHelper.isSameDays(System.currentTimeMillis(), item.date)) {
                        DateHelper.convertLongToDate(item.date)
                    } else DateHelper.convertLongToTime(item.date)
            }
        }
    }

    private class NoCommentsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            itemView.noCommentsTextView.text = itemView.context.getString(R.string.comments_not_exists)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == COMMENTS_EXISTS) {
            CommentsVH(LayoutInflater.from(parent.context).inflate(R.layout.comments_item, parent, false))
        } else {
            NoCommentsVH(LayoutInflater.from(parent.context).inflate(R.layout.empty_comments_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (newsList.first().id == EMPTY_COMMENTS) {
            NO_COMMENTS
        } else {
            COMMENTS_EXISTS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == COMMENTS_EXISTS) {
            (holder as CommentsVH).bind(newsList[position])
        } else {
            (holder as NoCommentsVH).bind()
        }
    }

    override fun getItemCount(): Int = newsList.size

    private companion object {
        const val COMMENTS_EXISTS = 0
        const val NO_COMMENTS = 1
        const val EMPTY_COMMENTS = -1
    }
}
