package com.vkpractice.app.data.network

import com.vkpractice.app.data.network.responses.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @autor d.snytko
 */
interface VkRestApi {
    @GET("/method/newsfeed.get?&filter=post&owner_id")
    fun getPostsFromNetwork(): Single<Response<PostResponse>>

    @FormUrlEncoded
    @POST("/method/likes.add?&type=post")
    fun likePost(
        @Field("item_id") postId: Int?,
        @Field("owner_id") ownerId: Int?
    ): Completable

    @FormUrlEncoded
    @POST("/method/likes.delete?&type=post")
    fun disLikePost(
        @Field("item_id") postId: Int?,
        @Field("owner_id") ownerId: Int?
    ): Completable

    @FormUrlEncoded
    @POST("/method/wall.getComments?extended=true")
    fun getCommentsForPost(
        @Field("post_id") postId: Int?,
        @Field("owner_id") ownerId: Int?
    ): Single<Response<CommentsResponse>>

    @FormUrlEncoded
    @POST("/method/wall.createComment")
    fun leaveComment(
        @Field("post_id") postId: Int?,
        @Field("owner_id") ownerId: Int?,
        @Field("message") message: String?
    ): Completable

    @GET("/method/users.get?&fields=bdate, city,  country, first_name, last_name, home_town, photo_max, photo_max_orig, career, online, domain, education, last_seen, followers_count, nickname, connections, about, quotes, screen_name")
    fun getProfileInfo(): Single<Response<ProfileResponse>>

    @GET("/method/groups.get")
    fun getGroupsCount(): Single<Response<GroupsCountResponse>>

    @GET("/method/friends.get")
    fun getFriendsCount(): Single<Response<FriendsCountResponse>>

    @GET("method/wall.get?&filter=post")
    fun getPostsForCountProfile(): Single<Response<WallResponse>>

    @GET("/method/wall.get?&filter=post&count=100&extended=true")
    fun getPostsFromNetworkForProfile(): Single<Response<PostResponse>>

    @FormUrlEncoded
    @POST("/method/groups.getById")
    fun getGroupById(@Field("group_ids") groupIds: Int?): Single<Response<GroupResponse>>

    @FormUrlEncoded
    @POST("/method/wall.post")
    fun leavePost(@Field("message") message: String?): Single<Response<PostToWallResponse>>
}
