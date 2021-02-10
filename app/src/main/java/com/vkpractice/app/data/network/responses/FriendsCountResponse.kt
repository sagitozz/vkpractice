package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

/**
 * @autor d.snytko
 */
class FriendsCountResponse(

    @SerializedName("response")
    val response: FriendsCount
)

class FriendsCount(

    @SerializedName("count")
    val count: Int? = 0
)
