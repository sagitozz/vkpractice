package com.vkpractice.app.repo

import com.vkpractice.app.data.database.CacheTimePolicy
import com.vkpractice.app.data.database.dao.CacheTimeStampDao
import com.vkpractice.app.data.database.dao.PostsDao
import com.vkpractice.app.data.database.entities.CacheTimeStamp
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.network.NetworkMonitor
import com.vkpractice.app.data.network.NetworkService
import com.vkpractice.app.utils.extensions.toBoolean
import io.reactivex.Single
import javax.inject.Inject

/**
 * @autor d.snytko
 */
interface PostsRepository {

    fun loadAllPostsFromNetworkAndSetTimeStamp(): Single<List<PostModel>>

    fun getAllPostsFromDb(): Single<List<PostModel>>

    fun getNewsPostsFromDb(): Single<List<PostModel>>

    fun getWallPostsFromDb(): Single<List<PostModel>>

    fun getFavorites(): Single<List<PostModel>>

    fun dislikePost(item: PostModel): Single<List<PostModel>>

    fun likePost(item: PostModel): Single<List<PostModel>>

    fun deletePostFromDb(item: PostModel): Single<List<PostModel>>

    fun saveAllPostsToDb(posts: List<PostModel>): Single<List<PostModel>>

    fun loadData(): Single<List<PostModel>>
}

class PostsRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val timeoutCachePolicy: CacheTimePolicy,
    private val cacheDao: CacheTimeStampDao,
    private val postsDao: PostsDao,
    private val networkMonitor: NetworkMonitor
) : PostsRepository {

    override fun loadData(): Single<List<PostModel>> =
        getPostsSource()

    override fun getFavorites(): Single<List<PostModel>> =
        postsDao.getAllNewsPosts()
            .map { list -> list.filter { it.userLikes.toBoolean() } }

    override fun deletePostFromDb(item: PostModel): Single<List<PostModel>> =
        postsDao.delete(item)
            .andThen(postsDao.getAllNewsPosts())

    override fun getAllPostsFromDb(): Single<List<PostModel>> =
        postsDao.getAllPosts()

    override fun getNewsPostsFromDb(): Single<List<PostModel>> =
        postsDao.getAllNewsPosts()

    override fun getWallPostsFromDb(): Single<List<PostModel>> =
        postsDao.getAllWallPosts()

    override fun saveAllPostsToDb(posts: List<PostModel>): Single<List<PostModel>> =
        postsDao.insertAll(posts)
            .andThen(
                cacheDao
                    .insert(CacheTimeStamp(timeStamp = timeoutCachePolicy.getTimeStamp()))
            )
            .andThen(postsDao.getAllNewsPosts())

    override fun loadAllPostsFromNetworkAndSetTimeStamp(): Single<List<PostModel>> =
        networkService.getPosts()
            .doOnSuccess {
                postsDao.deleteAllNewsPosts().blockingGet()
                timeoutCachePolicy.setTimeStamp(System.currentTimeMillis())
            }
            .flatMap { saveAllPostsToDb(it) }

    override fun dislikePost(item: PostModel): Single<List<PostModel>> =
        postsDao.update(
            item.copy(
                userLikes = 0,
                postLikes = item.postLikes.minus(1)
            )
        )
            .andThen(networkService.disLikePost(item.postId, item.postOwnerId))
            .andThen(getAllPostsFromDb())

    override fun likePost(item: PostModel): Single<List<PostModel>> =
        postsDao.update(item.copy(userLikes = 1, postLikes = item.postLikes.plus(1)))
            .andThen(networkService.likePost(item.postId, item.postOwnerId))
            .andThen(getAllPostsFromDb())


    private fun getPostsSource(): Single<List<PostModel>> {
        return isCacheActual()
            .flatMap { isActual ->
                if (!isActual) {
                    if (networkMonitor.isNetworkAvailable()) {
                        return@flatMap loadAllPostsFromNetworkAndSetTimeStamp()
                    } else {
                        return@flatMap getNewsPostsFromDb()
                    }
                } else {
                    return@flatMap getNewsPostsFromDb()
                }
            }
    }

    private fun isCacheActual(): Single<Boolean> {
        return cacheDao.count()
            .flatMap {
                if (it > 0) {
                    return@flatMap cacheDao.getCacheTimeStamp()
                        .map { timeoutCachePolicy.setTimeStamp(it.timeStamp) }
                        .map { timeoutCachePolicy.isValid() }
                } else {
                    return@flatMap Single.just(false)
                }
            }
    }
}
