package com.vkpractice.app.ui.profileFragment

import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BasePresenter
import com.vkpractice.app.repo.PostsRepository
import com.vkpractice.app.repo.ProfileRepository
import com.vkpractice.app.utils.extensions.toBoolean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @autor d.snytko
 */
class ProfileFragmentPresenter @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val postsRepository: PostsRepository
) :
    BasePresenter<ProfileFragmentContract.View>(),
    ProfileFragmentContract.Presenter {

    override fun loadData() {
        presenterDisposable.add(
            profileRepository.getProfileInfo()
                .zipWith(profileRepository.getWallPosts()) { profile, posts -> profile to posts }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.onStateReceive(NetworkState.Loading) }
                .subscribe(
                    { data ->
                        if (data.first == emptyList<List<ProfileModel>>() || data.second == emptyList<List<PostModel>>()) {
                            viewState.onStateReceive(NetworkState.Error(IllegalStateException(NETWORK_ERROR)))
                        } else {
                            viewState.onStateReceive(NetworkState.ListLoaded(data))
                        }
                    },

                    { error -> viewState.onStateReceive(NetworkState.Error(error)) }
                )
        )
    }

    override fun checkForFavorites() {
        presenterDisposable.add(
            postsRepository.getNewsPostsFromDb()
                .map { list ->
                    list.any { it.userLikes.toBoolean() }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { show -> viewState.favoriteIconShow(show) },
                    { error -> viewState.onError(error) }
                )
        )
    }

    override fun likeItem(item: PostModel) {
        if (item.userLikes.toBoolean()) {
            dislikePost(item)
        } else {
            likePost(item)
        }
    }

    override fun sendPost(message: String) {
        presenterDisposable.add(
            profileRepository.leavePost(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { posts -> viewState.updatePostAdapter(posts) },
                    { error -> viewState.onError(error) })
        )
    }

    private fun likePost(workingPost: PostModel) {
        presenterDisposable.add(
            postsRepository.likePost(workingPost)
                .flatMap { postsRepository.getWallPostsFromDb() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newsList ->
                        viewState.updatePostAdapter(newsList)
                    },
                    { error -> viewState.onError(error) })
        )
    }

    private fun dislikePost(item: PostModel) {
        presenterDisposable.add(
            postsRepository.dislikePost(item)
                .flatMap { postsRepository.getWallPostsFromDb() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newsList ->
                        viewState.updatePostAdapter(newsList)
                    },
                    { error -> viewState.onError(error) })
        )
    }

    private companion object {

        const val NETWORK_ERROR = "Проверьте подключение к сети"
    }
}
