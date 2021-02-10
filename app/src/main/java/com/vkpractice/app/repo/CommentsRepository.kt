package com.vkpractice.app.repo

import com.vkpractice.app.data.database.dao.CommentsDao
import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.network.NetworkMonitor
import com.vkpractice.app.data.network.NetworkService
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @autor d.snytko
 */
interface CommentsRepository {

    fun loadComments(item: PostModel): Single<List<CommentsModel>>

    fun sendComment(postId: Int?, ownerId: Int?, message: String): Completable

    fun getCommentsSource(item: PostModel): Single<List<CommentsModel>>
}

class CommentsRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val commentsDao: CommentsDao,
    private val networkMonitor: NetworkMonitor,
) : CommentsRepository {

    override fun loadComments(item: PostModel): Single<List<CommentsModel>> {
        return getCommentsSource(item)
    }

    override fun sendComment(postId: Int?, ownerId: Int?, message: String): Completable =
        networkService.sendComment(postId, ownerId, message)

    override fun getCommentsSource(item: PostModel): Single<List<CommentsModel>> {
        return Single.just(networkMonitor.isNetworkAvailable())
            .flatMap { networkAvailable ->
                if (!networkAvailable) {
                    return@flatMap commentsDao.getCommentsToPost(item.postId)
                } else {
                    return@flatMap networkService.getComments(item)
                        .doOnSuccess { comments ->
                            commentsDao.deleteAllComments()
                            commentsDao.insertAll(comments).blockingGet()
                        }
                }
            }
    }
}
