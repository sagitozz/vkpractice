package com.vkpractice.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

/**
 * @autor d.snytko
 */
object ImageLoader {

    fun loadImage(url: String?, targetView: ImageView) {
        Glide
            .with(targetView)
            .load(url)
            .apply(
                RequestOptions()
                    .override(SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_ARGB_8888)
            )
            .fitCenter()
            .into(targetView)
    }

    fun loadAvatarImage(url: String?, targetView: ImageView) {
        Glide
            .with(targetView)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(
                RequestOptions()
                    .circleCrop()
                    .override(SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_ARGB_8888)
            )
            .into(targetView)
    }

    fun getImageFromUrl(imageUrl: String, context: Context): Bitmap {
        return Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .submit()
            .get()
    }
}
