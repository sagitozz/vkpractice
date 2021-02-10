package com.vkpractice.app.ui.detailsFragment

import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @autor d.snytko
 */
interface DetailsPageContract {

    interface Presenter {

        fun loadComments(post: PostModel)

        fun likeItem(post: PostModel)

        fun sendComment(post: PostModel, message: String)
    }

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    interface View : BaseView<NetworkState<*>> {

        @StateStrategyType(SingleStateStrategy::class)
        fun updateAdapter(post: PostModel, comments: List<CommentsModel>)

        @StateStrategyType(SingleStateStrategy::class)
        fun onStateReceive(state: NetworkState<List<CommentsModel>>)

        fun showIdle(show: Boolean)

        @StateStrategyType(SingleStateStrategy::class)
        fun onError(error: Throwable?)
    }
}
