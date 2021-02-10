package com.vkpractice.app.data.network.responses

import com.google.gson.annotations.SerializedName

/**
 * @autor d.snytko
 */
class PostToWallResponse(

    @SerializedName("response")
    val response: PostWallResponse
)

class PostWallResponse(

    @SerializedName("post_id")
    val postId: Int?
)

