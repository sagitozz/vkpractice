package com.vkpractice.app.ui.profileFragment

import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.data.network.responses.CareerItem
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @autor d.snytko
 */
interface ProfileFragmentContract {

    interface Presenter {

        fun loadData()

        fun likeItem(item: PostModel)

        fun sendPost(message: String)

        fun checkForFavorites()
    }

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    interface View : BaseView<NetworkState<*>> {

        @StateStrategyType(SingleStateStrategy::class)
        fun updateProfileAdapter(profile: List<ProfileModel>)

        @StateStrategyType(SingleStateStrategy::class)
        fun updatePostAdapter(posts: List<PostModel>)

        @StateStrategyType(SingleStateStrategy::class)
        fun updateCareerAdapter(careers: List<CareerItem>)

        @StateStrategyType(SingleStateStrategy::class)
        fun onStateReceive(state: NetworkState<Pair<List<ProfileModel>, List<PostModel>>>)

        fun showIdle(show: Boolean)

        @StateStrategyType(SingleStateStrategy::class)
        fun onError(error: Throwable?)

        fun favoriteIconShow(show: Boolean)
    }
}
