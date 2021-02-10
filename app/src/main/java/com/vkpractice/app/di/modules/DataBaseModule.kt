package com.vkpractice.app.di.modules

import android.content.Context
import androidx.room.Room
import com.vkpractice.app.data.database.AppDatabase
import com.vkpractice.app.data.database.CacheTimePolicy
import com.vkpractice.app.data.database.CacheTimePolicyImpl
import com.vkpractice.app.data.database.dao.CacheTimeStampDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @autor d.snytko
 */
@Module
interface DataBaseModule {

    @Singleton
    @Binds
    fun bindsCacheTimePolicy(impl: CacheTimePolicyImpl): CacheTimePolicy

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "VK_DB")
                .build()
        }

        @Provides
        @Singleton
        fun provideCacheDao(database: AppDatabase): CacheTimeStampDao =
            database.getCacheTimeStampDao()
    }
}
