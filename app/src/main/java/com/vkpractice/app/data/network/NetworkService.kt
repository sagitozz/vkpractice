package com.vkpractice.app.data.network

import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.data.mappers.CommentsMapper
import com.vkpractice.app.data.mappers.PostsMapper
import com.vkpractice.app.data.mappers.ProfileMapper
import com.vkpractice.app.data.network.responses.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

/**
 * @autor d.snytko
 */
interface NetworkService {

    fun getPosts(): Single<List<PostModel>>

    fun getProfilePosts(): Single<List<PostModel>>

    fun getComments(item: PostModel): Single<List<CommentsModel>>

    fun sendComment(postId: Int?, ownerId: Int?, message: String?): Completable

    fun likePost(postId: Int?, ownerId: Int?): Completable

    fun disLikePost(postId: Int?, ownerId: Int?): Completable

    fun getProfileData(): Single<List<ProfileModel>>

    fun sendPost(message: String): Single<Response<PostToWallResponse>>
}

class NetworkServiceImpl @Inject constructor(
    private val vkRestApi: VkRestApi,
    private val postsMapper: PostsMapper,
    private val commentsMapper: CommentsMapper,
    private val profileMapper: ProfileMapper
) : NetworkService {

    override fun getPosts(): Single<List<PostModel>> =
        vkRestApi.getPostsFromNetwork()
            .map {
                val groups = it.body()?.response?.groups ?: return@map emptyList()
                val posts = it.body()?.response?.items ?: return@map emptyList()
                val profiles = it.body()?.response?.profiles ?: return@map emptyList()
                val postFromGroups = posts.filter { post -> post.sourceId < 0 }
                postsMapper(groups, postFromGroups, profiles, NEWS_POSTS)
            }

    override fun getProfilePosts(): Single<List<PostModel>> {
        return vkRestApi.getPostsFromNetworkForProfile()
            .map {
                val groups = it.body()?.response?.groups ?: return@map emptyList()
                val posts = it.body()?.response?.items ?: return@map emptyList()
                val profiles = it.body()?.response?.profiles ?: return@map emptyList()
                postsMapper(groups, posts, profiles, WALL_POSTS)
            }
    }

    override fun getComments(item: PostModel): Single<List<CommentsModel>> {
        return vkRestApi.getCommentsForPost(item.postId, item.postOwnerId)
            .map {commentsResponse->
                val profiles = commentsResponse.body()?.response?.profiles ?: return@map emptyList()
                val comments = commentsResponse.body()?.response?.items ?: return@map emptyList()
                commentsMapper(profiles, comments)
            }
    }

    override fun sendComment(
        postId: Int?,
        ownerId: Int?,
        message: String?
    ): Completable = vkRestApi.leaveComment(postId, ownerId, message)

    override fun likePost(postId: Int?, ownerId: Int?): Completable = vkRestApi.likePost(postId, ownerId)

    override fun disLikePost(postId: Int?, ownerId: Int?): Completable = vkRestApi.disLikePost(postId, ownerId)

    override fun sendPost(message: String): Single<Response<PostToWallResponse>> = vkRestApi.leavePost(message)

    override fun getProfileData(): Single<List<ProfileModel>> {
        return Single.zip(
            vkRestApi.getProfileInfo(),
            vkRestApi.getFriendsCount(),
            vkRestApi.getPostsForCountProfile(),
            vkRestApi.getGroupsCount(),
        ) { profileInfo, friendsCount, postsCount, groupsCount ->
            mapProfile(profileInfo, friendsCount, postsCount, groupsCount)
        }
    }

    private fun mapProfile(
        profileInfo: Response<ProfileResponse>,
        friendsCount: Response<FriendsCountResponse>,
        postsCount: Response<WallResponse>,
        groupsCount: Response<GroupsCountResponse>
    ): List<ProfileModel> {
        val profile = profileInfo.body()?.response
        val friends = friendsCount.body()?.response?.count
        val posts = postsCount.body()?.response?.count
        val groups = groupsCount.body()?.response?.count

        return profileMapper(
            profile,
            groups,
            friends,
            posts,
            getCareerList(profile)
        )
    }

    private fun getCareerList(profile: List<ProfileResponseItem>?): MutableList<CareerItem> {
        val careerList = mutableListOf<CareerItem>()
        profile?.first()?.career?.forEach { careerItem ->
            val groupName =
                careerItem?.groupId.let { group -> vkRestApi.getGroupById(group).blockingGet() }
            if (careerItem != null) {
                careerList.add(
                    CareerItem(
                        groupName = groupName.body()?.response?.first()?.name,
                        groupPhoto = groupName.body()?.response?.first()?.photo100,
                        from = careerItem.from,
                        untilDate = careerItem.untilDate,
                        position = careerItem.position,
                        groupId = 0
                    )
                )
            }
        }
        return careerList
    }

    companion object {
        private const val NEWS_POSTS = -1
        private const val WALL_POSTS = 1
    }
}
