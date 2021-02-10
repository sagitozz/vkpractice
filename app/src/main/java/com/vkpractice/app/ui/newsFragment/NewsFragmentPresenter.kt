package com.vkpractice.app.ui.newsFragment

import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BasePresenter
import com.vkpractice.app.repo.PostsRepository
import com.vkpractice.app.utils.extensions.toBoolean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import javax.inject.Inject

/**
 * @autor d.snytko
 */
@InjectViewState
class NewsFragmentPresenter @Inject constructor(private val repository: PostsRepository) :
    BasePresenter<NewsPageContract.View>(),
    NewsPageContract.Presenter {

    override fun loadDataFromRepository() {
        presenterDisposable.add(
            repository.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.onStateReceive(NetworkState.Loading) }
                .subscribe({ list -> handleResult(list) },
                    { error -> viewState.onError(error) }
                )
        )
    }

    override fun loadDataFromNetwork() {
        presenterDisposable.add(
            repository.loadAllPostsFromNetworkAndSetTimeStamp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.onStateReceive(NetworkState.Loading) }
                .subscribe(
                    { posts ->
                        handleResult(posts)
                    },
                    { error ->
                        viewState.onStateReceive(NetworkState.Error(error))
                    })
        )
    }

    private fun handleResult(posts: List<PostModel>) {
        checkForFavorites()
        viewState.onStateReceive(NetworkState.ListLoaded(posts))
    }

    override fun removeItem(item: PostModel) {
        presenterDisposable.add(
            repository.deletePostFromDb(item)
                .flatMap { repository.getNewsPostsFromDb() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newsList ->
                        handleResult(newsList)
                    }, { error -> viewState.onError(error) }
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

    private fun likePost(workingPost: PostModel) {
        presenterDisposable.add(
            repository.likePost(workingPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newsList ->
                        handleResult(newsList)
                    },
                    { error -> viewState.onError(error) })
        )
    }

    private fun dislikePost(item: PostModel) {
        presenterDisposable.add(
            repository.dislikePost(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { newsList ->
                        handleResult(newsList)
                    },
                    { error -> viewState.onError(error) })
        )
    }

    fun checkForFavorites() {
        presenterDisposable.add(
            repository.getNewsPostsFromDb()
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
}
