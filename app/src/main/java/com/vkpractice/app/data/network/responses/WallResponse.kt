package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

class WallResponse(

	@SerializedName("response")
	val response: WallCountResponse
)

class WallCountResponse(

	@SerializedName("count")
	val count: Int
)
