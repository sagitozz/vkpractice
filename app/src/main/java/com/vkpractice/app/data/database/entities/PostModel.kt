package com.vkpractice.app.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @autor d.snytko
 */
@Parcelize
@Entity(tableName = "vk_item_table")
data class PostModel(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "postId")
    val postId: Int,

    @ColumnInfo(name = "groupName")
    val groupName: String? = null,

    @ColumnInfo(name = "groupAvatar")
    val groupAvatar: String?,

    @ColumnInfo(name = "postDate")
    val postDate: Long,

    @ColumnInfo(name = "postText")
    val postText: String?,

    @ColumnInfo(name = "postImage")
    val postImage: String,

    @ColumnInfo(name = "postViews")
    val postViews: Int = 0,

    @ColumnInfo(name = "postLikes")
    val postLikes: Int = 0,

    @ColumnInfo(name = "postOwnerId")
    val postOwnerId: Int = 0,

    @ColumnInfo(name = "userLikes")
    val userLikes: Int = 0,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,

    @ColumnInfo(name = "comments")
    val comments: Int = 0,

    @ColumnInfo(name = "firstName")
    val firstName: String?,

    @ColumnInfo(name = "lastName")
    val lastName: String?,

    @ColumnInfo(name = "canPost")
    val canPost: Int?,

    @ColumnInfo(name = "wallPost")
    val wallPost: Boolean
) : Parcelable
