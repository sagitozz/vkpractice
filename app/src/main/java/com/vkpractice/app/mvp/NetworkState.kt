package com.vkpractice.app.mvp

/**
 * @autor d.snytko
 */
sealed class NetworkState<out T> {
    data class ListLoaded<out T>(val data: T) : NetworkState<T>()
    data class Error(val error: Throwable) : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
}
