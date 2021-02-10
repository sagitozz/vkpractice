package com.vkpractice.app.utils.extensions

import android.content.Context

/**
 * @autor d.snytko
 */
fun Context.dpToPx(dp: Int) = (this.resources.displayMetrics.density * dp).toInt()
