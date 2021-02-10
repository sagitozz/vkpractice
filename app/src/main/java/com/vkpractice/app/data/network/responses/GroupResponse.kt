package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

class GroupResponse(

	@SerializedName("response")
	val response: List<ResponseItem> = emptyList()
)

class ResponseItem(

	@SerializedName("name")
	val name: String?,

	@SerializedName("id")
	val id: Int?,

	@SerializedName("type")
	val type: String?,

	@SerializedName("photo_100")
	val photo100: String?,
)
