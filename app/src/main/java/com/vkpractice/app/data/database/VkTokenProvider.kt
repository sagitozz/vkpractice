package com.vkpractice.app.data.database

import android.content.SharedPreferences
import com.vkpractice.app.ui.LoginActivity
import javax.inject.Inject

/**
 * @autor d.snytko
 */
class VkTokenProvider @Inject constructor(private val prefs: SharedPreferences) {

    fun getToken() : String = prefs.getString(LoginActivity.Session.TOKEN_NAME, LoginActivity.Session.EMPTY_TOKEN).toString()
}
