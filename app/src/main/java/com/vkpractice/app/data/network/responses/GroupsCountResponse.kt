package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

/**
 * @autor d.snytko
 */
class GroupsCountResponse(

    @SerializedName("response")
    val response: GroupsCount
)

class GroupsCount(

    @SerializedName("count")
    val count: Int = 0
)
