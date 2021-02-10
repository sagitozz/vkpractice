package com.vkpractice.app.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vkpractice.app.R
import com.vkpractice.app.mvp.base.BaseActivity
import com.vkpractice.app.ui.favoriteFragment.FavoriteFragment
import com.vkpractice.app.ui.newsFragment.NewsFragment
import com.vkpractice.app.ui.profileFragment.ProfileFragment
import com.vkpractice.app.utils.ImageHelper
import com.vkpractice.app.utils.ImageLoader
import com.vkpractice.app.utils.NavBarHandler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*
import java.io.IOException


class MainActivity : BaseActivity(R.layout.activity_main), NavBarHandler, ImageHelper {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) initFirstFragment()
    }

    override fun showError(error: Throwable?) {
        error_layout.retryButton.setOnClickListener { error_layout.isVisible = false }
        error_layout.isVisible = true
        showErrorToast(error)
    }

    override fun navBarSetVisible(show: Boolean) {
        bottomNav.isVisible = show
    }

    override fun navigateFromFavorite() {
        bottomNav.menu.getItem(POSITION_FAVORITE).isVisible = false
        navigateToNewsFragment()
    }

    override fun favoriteIconShow(show: Boolean) {
        bottomNav.menu.getItem(POSITION_FAVORITE).isVisible = show
    }

    override fun savePhotoFile(file: String) {
        activityDisposable.add(
            Single.just(file)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it != null) {
                            ImageLoader.getImageFromUrl(it, this)
                                .let { bitmap -> saveImage(bitmap) }
                        }
                    },
                    { error ->
                        showError(error)
                    })
        )
    }

    override fun sharePhoto(url: String) {
        activityDisposable.add(
            Single.fromCallable {
                ImageLoader.getImageFromUrl(url, this)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { bitmap ->
                        shareImage(bitmap)
                    },
                    { error ->
                        showError(error)
                    }
                )
        )
    }

    private fun initFirstFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, NewsFragment.newInstance())
            .commitAllowingStateLoss()
    }

    private fun Context.saveImage(photo: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.MediaColumns.MIME_TYPE, getString(R.string.image_mime_type))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            compressPhoto(uri, photo)
        } catch (e: IOException) {
            if (uri != null) {
                contentResolver.delete(uri, null, null)
            }
            throw IOException(e)
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            runOnUiThread {
                Toast.makeText(this, getString(R.string.image_saved), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun Context.shareImage(photo: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.MediaColumns.MIME_TYPE, getString(R.string.image_mime_type))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            compressPhoto(uri, photo)
            sendPhoto(uri)
        } catch (e: IOException) {
            if (uri != null) {
                contentResolver.delete(uri, null, null)
            }
            throw IOException(e)
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
    }

    private fun compressPhoto(uri: Uri?, photo: Bitmap) =
        uri?.let {
            val stream = contentResolver.openOutputStream(uri)
            if (!photo.compress(Bitmap.CompressFormat.JPEG, COMPRESS_RATE, stream)) {
                throw IOException(getString(R.string.failed_to_save_bitmap))
            }

        } ?: throw IOException(getString(R.string.failed_to_create_new_mediastore))

    private fun sendPhoto(uri: Uri?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = getString(R.string.image_mime_type)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.allPosts -> {
                    return@OnNavigationItemSelectedListener navigateToNewsFragment()
                }
                R.id.favoritePosts -> {
                    return@OnNavigationItemSelectedListener navigateToFavoriteFragment()
                }
                R.id.profile -> {
                    return@OnNavigationItemSelectedListener navigateToProfileFragment()
                }
            }
            false
        }

    private fun navigateToNewsFragment(): Boolean {
        if (!bottomNav.menu.getItem(POSITION_NEWS).isChecked)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_fade_enter, R.anim.fragment_fade_exit,
                    R.anim.fragment_fade_enter, R.anim.fragment_fade_exit
                )
                .replace(R.id.fragmentContainer, NewsFragment.newInstance())
                .commitAllowingStateLoss()
        bottomNav.menu.getItem(POSITION_NEWS).isChecked = true
        return true

    }

    private fun navigateToFavoriteFragment(): Boolean {
        if (!bottomNav.menu.getItem(POSITION_FAVORITE).isChecked)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_fade_enter, R.anim.fragment_fade_exit,
                    R.anim.fragment_fade_enter, R.anim.fragment_fade_exit
                )
                .replace(R.id.fragmentContainer, FavoriteFragment.newInstance())
                .commitAllowingStateLoss()
        return true
    }

    private fun navigateToProfileFragment(): Boolean {
        if (!bottomNav.menu.getItem(POSITION_PROFILE).isChecked)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_fade_enter, R.anim.fragment_fade_exit,
                    R.anim.fragment_fade_enter, R.anim.fragment_fade_exit
                )
                .replace(R.id.fragmentContainer, ProfileFragment.newInstance())
                .commitAllowingStateLoss()
        return true
    }

    private companion object {

        const val POSITION_NEWS = 0
        const val POSITION_FAVORITE = 1
        const val POSITION_PROFILE = 2
        const val COMPRESS_RATE = 100
    }
}
