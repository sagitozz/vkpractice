package com.vkpractice.app.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vkpractice.app.R
import com.vkpractice.app.ui.LoginActivity.Session.EMPTY_TOKEN
import com.vkpractice.app.ui.LoginActivity.Session.SHARED_PREF_NAME
import com.vkpractice.app.ui.LoginActivity.Session.TOKEN_NAME
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope


class LoginActivity : AppCompatActivity(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfTokenExistsAndLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                getAndSaveToken(token)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onLoginFailed(errorCode: Int) {
                VK.login(this@LoginActivity, arrayListOf(VKScope.WALL, VKScope.FRIENDS, VKScope.OFFLINE))
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun checkIfTokenExistsAndLogin() {
        val pref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        if (pref.getString(TOKEN_NAME, EMPTY_TOKEN) == EMPTY_TOKEN || !VK.isLoggedIn()) {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS, VKScope.OFFLINE))
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun getAndSaveToken(token: VKAccessToken) {
        val pref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        if (pref.getString(TOKEN_NAME, EMPTY_TOKEN) == EMPTY_TOKEN) {
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putString(TOKEN_NAME, token.accessToken)
            editor.apply()
        }
    }

    object Session {

        const val SHARED_PREF_NAME = "TOKEN"
        const val TOKEN_NAME = "VK_ACCESS_TOKEN"
        const val EMPTY_TOKEN = "NO_TOKEN"
    }
}
