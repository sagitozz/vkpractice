package com.vkpractice.app.utils.extensions

/**
 * @autor d.snytko
 */

fun Int?.toBoolean(): Boolean =
    when (this) {
        null, 0 -> false
        1 -> true
        else -> throw IllegalStateException("Vale can be only 1 or 0")
    }

fun Int?.orZero(): Int = this ?: 0
