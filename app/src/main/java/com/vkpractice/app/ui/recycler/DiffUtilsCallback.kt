package com.vkpractice.app.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.data.network.responses.CareerItem

/**
 * @autor d.snytko
 */
class PostsDiffUtilsCallback : DiffUtil.ItemCallback<PostModel>() {
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel) =
        oldItem.postId == newItem.postId

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem == newItem
    }
}

class CommentsDiffUtilsCallback : DiffUtil.ItemCallback<CommentsModel>() {
    override fun areItemsTheSame(oldItem: CommentsModel, newItem: CommentsModel) =
        oldItem.postId == newItem.postId

    override fun areContentsTheSame(oldItem: CommentsModel, newItem: CommentsModel): Boolean {
        return oldItem == newItem
    }
}

class ProfileDiffUtilsCallback : DiffUtil.ItemCallback<ProfileModel>() {
    override fun areItemsTheSame(oldItem: ProfileModel, newItem: ProfileModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ProfileModel, newItem: ProfileModel): Boolean {
        return oldItem == newItem
    }
}

class CareerDiffUtilsCallback : DiffUtil.ItemCallback<CareerItem>() {
    override fun areItemsTheSame(oldItem: CareerItem, newItem: CareerItem) =
        oldItem.groupName == newItem.groupName

    override fun areContentsTheSame(oldItem: CareerItem, newItem: CareerItem): Boolean {
        return oldItem == newItem
    }
}
