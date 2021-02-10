package com.vkpractice.app.ui.newsFragment

import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @autor d.snytko
 */
interface NewsPageContract {

    interface Presenter {

        fun loadDataFromRepository()

        fun loadDataFromNetwork()

        fun removeItem(item: PostModel)

        fun likeItem(item: PostModel)
    }

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    interface View : BaseView<NetworkState<*>> {

        @StateStrategyType(AddToEndStrategy::class)
        fun updateAdapter(posts: List<PostModel>)

        @StateStrategyType(AddToEndStrategy::class)
        fun onStateReceive(state: NetworkState<List<PostModel>>)

        fun showIdle(show: Boolean)

        @StateStrategyType(AddToEndStrategy::class)
        fun onError(error: Throwable?)

        fun favoriteIconShow(show: Boolean)
    }
}
