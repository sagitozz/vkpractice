package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

class CommentsResponse(

	@SerializedName("response")
	val response: CommentsExtendedResponse
)

class CommentsItemsItem(

    @SerializedName("date")
	val date: Int,

    @SerializedName("from_id")
	val fromId: Int,

    @SerializedName("attachments")
	val attachments: List<CommentsAttachmentsItem>,

    @SerializedName("post_id")
	val postId: Int,

    @SerializedName("id")
	val id: Int,

    @SerializedName("text")
	val text: String,
)

class CommentsProfilesItem(

	@SerializedName("photo_50")
	val photo50: String,

	@SerializedName("last_name")
	val lastName: String,

	@SerializedName("id")
	val id: Int,

	@SerializedName("first_name")
	val firstName: String,
)

class CommentsExtendedResponse(

    @SerializedName("count")
	val count: Int,

    @SerializedName("profiles")
	val profiles: List<CommentsProfilesItem>,

    @SerializedName("items")
	val items: List<CommentsItemsItem>
)

class CommentsAttachmentsItem(

    @SerializedName("type")
	val type: String
)
