package com.vkpractice.app.data.database

import javax.inject.Inject

/**
 * @autor d.snytko
 */
private const val CACHE_TIMEOUT = 3000 * 60L

interface CacheTimePolicy {
    fun isValid(): Boolean
    fun setTimeStamp(timeStamp: Long)
    fun getTimeStamp(): Long
}

class CacheTimePolicyImpl @Inject constructor() : CacheTimePolicy {
    private var lastKnownTimeStamp = 0L

    override fun isValid(): Boolean =
        System.currentTimeMillis() - lastKnownTimeStamp < CACHE_TIMEOUT


    override fun setTimeStamp(timeStamp: Long) {
        lastKnownTimeStamp = timeStamp
    }

    override fun getTimeStamp():Long = lastKnownTimeStamp
}
