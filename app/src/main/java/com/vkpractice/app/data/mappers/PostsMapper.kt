package com.vkpractice.app.data.mappers

import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.network.responses.ContentItem
import com.vkpractice.app.data.network.responses.GroupsItem
import com.vkpractice.app.data.network.responses.ProfilesItem
import com.vkpractice.app.utils.extensions.orZero
import com.vkpractice.app.utils.extensions.toBoolean
import javax.inject.Inject

/**
 * @autor d.snytko
 */
class PostsMapper @Inject constructor() {

    operator fun invoke(
        groupsItem: List<GroupsItem>,
        postItems: List<ContentItem>,
        profilesItem: List<ProfilesItem>,
        multiplier: Int
    ): List<PostModel> {
        val listForAdapter = mutableListOf<PostModel>()
        postItems.forEach { post ->
            val group: GroupsItem? = groupsItem.find { group -> group.id == (post.sourceId * multiplier) }
            val profile: ProfilesItem? = profilesItem.find { profile -> profile.id == post.fromId }
            val (tPostId, tOwnerId, tViewsCount) =
                checkItNewsPostOrWallPostAndSetFields(post)
            val tPhoto = group?.photo100 ?: profile?.photo100
            listForAdapter.add(
                PostModel(
                    postId = tPostId,
                    groupName = group?.name,
                    groupAvatar = tPhoto,
                    postDate = post.date * MILLISECONDS_MULTIPLIER,
                    postText = post.text,
                    postImage = post.attachments?.firstOrNull()?.photo?.sizes?.last()?.url.orEmpty(),
                    postViews = tViewsCount,
                    postLikes = post.likes?.count.orZero(),
                    postOwnerId = tOwnerId,
                    userLikes = post.likes?.userLikes.orZero(),
                    isFavorite = post.likes?.userLikes.toBoolean(),
                    comments = post.comments?.count.orZero(),
                    firstName = profile?.firstName,
                    lastName = profile?.lastName,
                    canPost = post.comments?.canPost,
                    wallPost = post.postId == 0
                )
            )
        }
        return listForAdapter.filter { !it.postText.isNullOrEmpty() || it.postImage.isNotEmpty() }
    }

    private fun checkItNewsPostOrWallPostAndSetFields(
        post: ContentItem
    ): Triple<Int, Int, Int> {
        val tPostId = if (post.postId == 0) post.wallPostId else post.postId
        val tOwnerId = if (post.sourceId == 0) post.fromId else post.sourceId
        val tViewsCount = if (post.views == null) 0 else post.views.count.orZero()
        return Triple(tPostId, tOwnerId, tViewsCount)
    }

    companion object {
        const val MILLISECONDS_MULTIPLIER = 1000L
    }
}
