package com.vkpractice.app.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.vkpractice.app.application.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @autor d.snytko
 */
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return App.applicationContext()
    }

    @Provides
    fun providePref(context: Context): SharedPreferences = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
}
