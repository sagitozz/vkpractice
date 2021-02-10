package com.vkpractice.app.data.network.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class ProfileResponse(

	@SerializedName("response")
	val response: List<ProfileResponseItem>
)

class Country(

	@SerializedName("id")
	val id: Int?,

	@SerializedName("title")
	val title: String?
)


class City(

	@SerializedName("id")
	val id: Int?,

	@SerializedName("title")
	val title: String?
)

class LastSeen(

	@SerializedName("time")
	val time: Int,
)

class ProfileResponseItem(

	@SerializedName("country")
	val country: Country?,

	@SerializedName("career")
	val career: List<CareerItem?>?,

	@SerializedName("bdate")
	val bdate: String?,

	@SerializedName("last_seen")
	val lastSeen: LastSeen,

	@SerializedName("city")
	val city: City?,

	@SerializedName("about")
	val about: String?,

	@SerializedName("last_name")
	val lastName: String?,

	@SerializedName("university_name")
	val universityName: String?,

	@SerializedName("photo_max")
	val photoMax: String?,

	@SerializedName("quotes")
	val quotes: String?,

	@SerializedName("faculty")
	val faculty: Int?,

	@SerializedName("photo_max_orig")
	val photoMaxOrig: String?,

	@SerializedName("faculty_name")
	val facultyName: String?,

	@SerializedName("screen_name")
	val screenName: String?,

	@SerializedName("graduation")
	val graduation: Int?,

	@SerializedName("domain")
	val domain: String?,

	@SerializedName("nickname")
	val nickname: String?,

	@SerializedName("online")
	val online: Int?,

	@SerializedName("id")
	val id: Int?,

	@SerializedName("first_name")
	val firstName: String?
)

@Parcelize
data class CareerItem(

	@SerializedName("group_id")
	val groupId: Int? = null,

	@SerializedName("from")
	val from: Int,

	@SerializedName("until")
	val untilDate: Int,

	@SerializedName("position")
	val position: String?,

	@SerializedName("groupName")
	val groupName : String?,

	@SerializedName("groupPhoto")
	val groupPhoto : String?
) : Parcelable

