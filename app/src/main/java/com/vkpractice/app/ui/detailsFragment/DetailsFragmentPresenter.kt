package com.vkpractice.app.ui.detailsFragment

import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BasePresenter
import com.vkpractice.app.repo.CommentsRepository
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
class DetailsFragmentPresenter @Inject constructor(
    private val postsRepository: PostsRepository,
    private val commentsRepository: CommentsRepository
) :
    BasePresenter<DetailsPageContract.View>(),
    DetailsPageContract.Presenter {

    private lateinit var commentsItems: List<CommentsModel>

    override fun loadComments(post: PostModel) {
        viewState.onStateReceive(NetworkState.Loading)
        presenterDisposable.add(
            commentsRepository.loadComments(post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.onStateReceive(NetworkState.Loading) }
                .subscribe(
                    { comments ->
                        viewState.onStateReceive(NetworkState.ListLoaded(comments))
                        commentsItems = comments
                    },
                    { error ->
                        viewState.onStateReceive(NetworkState.Error(error))
                    }
                )
        )
    }

    override fun likeItem(post: PostModel) {
        if (post.userLikes.toBoolean()) {
            dislikePost(post)
        } else {
            likePost(post)
        }
    }

    override fun sendComment(post: PostModel, message: String) {
        presenterDisposable.add(
            commentsRepository.sendComment(post.postId, post.postOwnerId, message)
                .andThen(commentsRepository.loadComments(post))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { commentList -> viewState.onStateReceive(NetworkState.ListLoaded(commentList)) },
                    { error -> viewState.onError(error) })
        )
    }

    private fun likePost(item: PostModel) {
        presenterDisposable.add(
            postsRepository.likePost(item)
                .subscribeOn(Schedulers.io())
                .map { postsList -> postsList.first { it.postId == item.postId } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { post -> viewState.updateAdapter(post, commentsItems) },
                    { error -> viewState.onError(error) })
        )
    }

    private fun dislikePost(item: PostModel) {
        presenterDisposable.add(
            postsRepository.dislikePost(item)
                .flatMap { postsRepository.getAllPostsFromDb() }
                .map { postsList -> postsList.first { it.postId == item.postId } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { post ->
                        viewState.updateAdapter(post, commentsItems)
                    },
                    { error -> viewState.onError(error) })
        )
    }
}

