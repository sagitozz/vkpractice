package com.vkpractice.app.di

import com.vkpractice.app.di.modules.*
import com.vkpractice.app.ui.detailsFragment.DetailsFragmentPresenter
import com.vkpractice.app.ui.favoriteFragment.FavoriteFragmentPresenter
import com.vkpractice.app.ui.newsFragment.NewsFragmentPresenter
import com.vkpractice.app.ui.profileFragment.ProfileFragmentPresenter
import dagger.Component
import javax.inject.Singleton

/**
 * @autor d.snytko
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DataBaseModule::class,
        PostsModule::class,
        ProfileModule::class]
)
interface AppComponent {

    fun getFavoritePresenter(): FavoriteFragmentPresenter

    fun getNewsPresenter(): NewsFragmentPresenter

    fun getDetailsPresenter(): DetailsFragmentPresenter

    fun getProfilePresenter(): ProfileFragmentPresenter
}

