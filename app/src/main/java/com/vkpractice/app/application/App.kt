package com.vkpractice.app.application

import android.app.Application
import android.content.Context
import com.vkpractice.app.di.AppComponent
import com.vkpractice.app.di.DaggerAppComponent

/**
 * @autor d.snytko
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.create()
    }

    companion object {

        lateinit var instance: App
        lateinit var appComponent: AppComponent

        fun applicationContext(): Context = instance
    }
}
