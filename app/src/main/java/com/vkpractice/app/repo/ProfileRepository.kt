package com.vkpractice.app.repo

import com.vkpractice.app.data.database.dao.PostsDao
import com.vkpractice.app.data.database.dao.ProfileDao
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.data.network.NetworkMonitor
import com.vkpractice.app.data.network.NetworkService
import io.reactivex.Single
import javax.inject.Inject

/**
 * @autor d.snytko
 */
interface ProfileRepository {

    fun getProfileInfo(): Single<List<ProfileModel>>

    fun getWallPosts(): Single<List<PostModel>>

    fun saveAllWallPostsToDbAndGet(posts: List<PostModel>): Single<List<PostModel>>

    fun saveProfileToDbAndGet(profile: List<ProfileModel>): Single<List<ProfileModel>>

    fun leavePost(message: String): Single<List<PostModel>>

    fun getProfileSource(): Single<List<ProfileModel>>

    fun getWallPostsSource(): Single<List<PostModel>>

}

class ProfileRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val postsDao: PostsDao,
    private val profileDao: ProfileDao,
    private val networkMonitor: NetworkMonitor
) : ProfileRepository {

    override fun getProfileInfo(): Single<List<ProfileModel>> = getProfileSource()

    override fun getWallPosts(): Single<List<PostModel>> {
        return getWallPostsSource()
    }

    override fun saveAllWallPostsToDbAndGet(posts: List<PostModel>): Single<List<PostModel>> =
        postsDao.deleteAllWallPosts()
            .andThen(postsDao.insertAll(posts))
            .andThen(postsDao.getAllWallPosts())


    override fun saveProfileToDbAndGet(profile: List<ProfileModel>): Single<List<ProfileModel>> =
        profileDao.deleteProfile()
            .andThen(profileDao.insertAll(profile))
            .andThen(profileDao.getProfile())

    override fun leavePost(message: String): Single<List<PostModel>> =
        networkService.sendPost(message)
            .flatMap { networkService.getProfilePosts() }
            .flatMap { saveAllWallPostsToDbAndGet(it) }

    override fun getProfileSource(): Single<List<ProfileModel>> {
        return Single.just(networkMonitor.isNetworkAvailable())
            .flatMap { networkAvailable ->
                if (!networkAvailable) {
                    return@flatMap profileDao.getProfile()
                } else {
                    return@flatMap networkService.getProfileData()
                        .flatMap { profileList ->
                            saveProfileToDbAndGet(profileList)
                        }
                }
            }
    }

    override fun getWallPostsSource(): Single<List<PostModel>> {
        return Single.just(networkMonitor.isNetworkAvailable())
            .flatMap { networkAvailable ->
                if (!networkAvailable) {
                    return@flatMap postsDao.getAllWallPosts()
                } else {
                    return@flatMap networkService.getProfilePosts()
                        .flatMap { postList ->
                            saveAllWallPostsToDbAndGet(postList)
                        }
                }
            }
    }
}
