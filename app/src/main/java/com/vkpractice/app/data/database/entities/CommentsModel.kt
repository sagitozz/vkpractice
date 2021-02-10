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
@Entity(tableName = "comments_table")
data class CommentsModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "postId")
    val postId: Int? = null,

    @ColumnInfo(name = "firstName")
    val firstName: String? = null,

    @ColumnInfo(name = "lastName")
    val lastName: String? = null,

    @ColumnInfo(name = "avatar")
    val avatar: String? = null,

    @ColumnInfo(name = "date")
    val date: Long = 0,

    @ColumnInfo(name = "text")
    val text: String? = null
) : Parcelable
