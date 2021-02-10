package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

class PostResponse(

    @SerializedName("response")
    val response: Response,
)

class Comments(

    @SerializedName("count")
    val count: Int,

    @SerializedName("can_post")
    val canPost: Int
)

class GroupsItem(

    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("photo_100")
    val photo100: String
)

class Likes(

    @SerializedName("user_likes")
    val userLikes: Int,

    @SerializedName("count")
    val count: Int = 0
)

class Photo(

    @SerializedName("date")
    val date: Int,

    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("sizes")
    val sizes: List<SizesItem> = emptyList(),

    @SerializedName("id")
    val id: Int,

    @SerializedName("text")
    val text: String
)

class ProfilesItem(

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("photo_100")
    val photo100: String,

    @SerializedName("first_name")
    val firstName: String
)

class Response(

    @SerializedName("profiles")
    val profiles: List<ProfilesItem>,

    @SerializedName("groups")
    val groups: List<GroupsItem> = emptyList(),

    @SerializedName("items")
    val items: List<ContentItem> = emptyList()
)

class SizesItem(

    @SerializedName("width")
    val width: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("height")
    val height: Int
)

class AttachmentsItem(

    @SerializedName("photo")
    val photo: Photo?,

    @SerializedName("type")
    val type: String
)

class ContentItem(

    @SerializedName("date")
    val date: Long,

    @SerializedName("attachments")
    val attachments: List<AttachmentsItem>? = emptyList(),

    @SerializedName("comments")
    val comments: Comments?,

    @SerializedName("source_id")
    val sourceId: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("id")
    val wallPostId: Int,

    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("from_id")
    val fromId: Int,

    @SerializedName("text")
    val text: String?,

    @SerializedName("views")
    val views: Views?,

    @SerializedName("likes")
    val likes: Likes?
)

class Views(

    @SerializedName("count")
    var count: Int = 0
)
