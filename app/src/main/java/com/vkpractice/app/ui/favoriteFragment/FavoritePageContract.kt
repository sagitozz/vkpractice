package com.vkpractice.app.ui.favoriteFragment

import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @autor d.snytko
 */
interface FavoritePageContract {

    interface Presenter {

        fun loadData()

        fun removeItem(item: PostModel)

        fun likeItem(item: PostModel)
    }

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    interface View : BaseView<NetworkState<*>> {

        @StateStrategyType(AddToEndStrategy::class)
        fun updateAdapter(favorites: List<PostModel>)

        fun navigateToNewsFragment()

        @StateStrategyType(AddToEndStrategy::class)
        fun onStateReceive(state: NetworkState<List<PostModel>>)

        fun showIdle(show: Boolean)

        @StateStrategyType(AddToEndStrategy::class)
        fun onError(error: Throwable?)
    }
}
