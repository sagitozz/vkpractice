package com.vkpractice.app.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vkpractice.app.data.network.responses.CareerItem
import kotlinx.android.parcel.Parcelize

/**
 * @autor d.snytko
 */
@Parcelize
@Entity(tableName = "profile_table")
data class ProfileModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = 0,

    @ColumnInfo(name = "firstName")
    val firstName: String?,

    @ColumnInfo(name = "lastName")
    val lastName: String?,

    @ColumnInfo(name = "avatar")
    val avatar: String?,

    @ColumnInfo(name = "country")
    val country: String?,

    @ColumnInfo(name = "city")
    val city: String?,

    @ColumnInfo(name = "lastSeen")
    val lastSeen: Long,

    @ColumnInfo(name = "birthday")
    val birthday: String?,

    @ColumnInfo(name = "about")
    val about: String?,

    @ColumnInfo(name = "universityName")
    val universityName: String? = null,

    @ColumnInfo(name = "quotes")
    val quotes: String?,

    @ColumnInfo(name = "faculty")
    val faculty: Int? = null,

    @ColumnInfo(name = "photoMaxOrig")
    val photoMaxOrig: String?,

    @ColumnInfo(name = "facultyName")
    val facultyName: String?,

    @ColumnInfo(name = "screenName")
    val screenName: String?,

    @ColumnInfo(name = "graduation")
    val graduation: Int?,

    @ColumnInfo(name = "domain")
    val domain: String?,

    @ColumnInfo(name = "nickname")
    val nickname: String?,

    @ColumnInfo(name = "online")
    val online: Int?,

    @ColumnInfo(name = "friendsCount")
    val friendsCount: Int?,

    @ColumnInfo(name = "postCount")
    val postCount: Int?,

    @ColumnInfo(name = "groupsCount")
    val groupsCount: Int?,

    @ColumnInfo(name = "profileCareer")
    val profileCareer: List<CareerItem>
) : Parcelable
