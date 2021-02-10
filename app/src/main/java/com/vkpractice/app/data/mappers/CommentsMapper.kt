package com.vkpractice.app.data.mappers

import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.network.responses.CommentsItemsItem
import com.vkpractice.app.data.network.responses.CommentsProfilesItem
import javax.inject.Inject

/**
 * @autor d.snytko
 */
class CommentsMapper @Inject constructor() {

    operator fun invoke(
        profiles: List<CommentsProfilesItem>,
        comments: List<CommentsItemsItem>
    ): List<CommentsModel> {
        val listForAdapter = mutableListOf<CommentsModel>()
        profiles.forEach { profile ->
            val commentsItems: CommentsItemsItem =
                comments.find { comments -> profile.id == comments.fromId } ?: return@forEach
            listForAdapter.add(
                CommentsModel(
                    postId = commentsItems.postId,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    avatar = profile.photo50,
                    date = commentsItems.date * MILLISECONDS_MULTIPLIER,
                    text = commentsItems.text
                )
            )
        }
        return listForAdapter.filter { it.text?.isNotEmpty() == true }
    }

    private companion object {
        const val MILLISECONDS_MULTIPLIER = 1000L
    }
}


