package com.vkpractice.app.ui.favoriteFragment


import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BasePresenter
import com.vkpractice.app.repo.PostsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import javax.inject.Inject

/**
 * @autor d.snytko
 */
@InjectViewState
class FavoriteFragmentPresenter @Inject constructor(private val repository: PostsRepository) :
    BasePresenter<FavoritePageContract.View>(),
    FavoritePageContract.Presenter {

    override fun loadData() {
        viewState.onStateReceive(NetworkState.Loading)
        presenterDisposable.add(
            repository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { posts -> viewState.onStateReceive(NetworkState.ListLoaded(posts)) },
                    { error -> viewState.onStateReceive(NetworkState.Error(error)) }
                )
        )
    }

    override fun removeItem(item: PostModel) {
        presenterDisposable.add(
            repository.dislikePost(item)
                .flatMap { repository.getFavorites() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { favoriteList ->
                        viewState.updateAdapter(favoriteList)
                        if (favoriteList.isEmpty()) {
                            viewState.navigateToNewsFragment()
                        }
                    },
                    { error -> viewState.onError(error) })
        )
    }

    override fun likeItem(item: PostModel) {
        presenterDisposable.add(
            repository.dislikePost(item)
                .flatMap { repository.getFavorites() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { favoriteList ->
                        viewState.updateAdapter(favoriteList)
                        if (favoriteList.isEmpty()) {
                            viewState.navigateToNewsFragment()
                        }
                    },
                    { error -> viewState.onError(error) })
        )
    }
}
