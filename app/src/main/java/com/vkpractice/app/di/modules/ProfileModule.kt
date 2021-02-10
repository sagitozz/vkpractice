package com.vkpractice.app.di.modules

import com.vkpractice.app.data.database.AppDatabase
import com.vkpractice.app.data.database.dao.ProfileDao
import com.vkpractice.app.repo.ProfileRepository
import com.vkpractice.app.repo.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @autor d.snytko
 */
@Module
interface ProfileModule {

    @Singleton
    @Binds
    fun bindsProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    companion object {
        @Provides
        @Singleton
        fun provideProfileDao(database: AppDatabase): ProfileDao {
            return database.getProfileDao()
        }
    }
}
