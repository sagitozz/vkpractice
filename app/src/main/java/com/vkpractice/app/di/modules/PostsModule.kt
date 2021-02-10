package com.vkpractice.app.di.modules

import com.vkpractice.app.data.database.AppDatabase
import com.vkpractice.app.data.database.dao.CommentsDao
import com.vkpractice.app.data.database.dao.PostsDao
import com.vkpractice.app.repo.CommentsRepository
import com.vkpractice.app.repo.CommentsRepositoryImpl
import com.vkpractice.app.repo.PostsRepository
import com.vkpractice.app.repo.PostsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @autor d.snytko
 */
@Module
interface PostsModule {

    @Singleton
    @Binds
    fun bindsPostRepository(impl: PostsRepositoryImpl): PostsRepository

    @Singleton
    @Binds
    fun bindsCommentsRepository(impl: CommentsRepositoryImpl): CommentsRepository

    companion object {

        @Provides
        @Singleton
        fun providePostsDao(database: AppDatabase): PostsDao {
            return database.getPostsDao()
        }

        @Provides
        @Singleton
        fun provideCommentsDao(database: AppDatabase): CommentsDao = database.getCommentsDao()
    }
}
