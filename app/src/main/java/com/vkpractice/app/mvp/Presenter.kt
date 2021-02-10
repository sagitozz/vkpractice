package com.vkpractice.app.mvp

import com.vkpractice.app.mvp.base.BaseView

/**
 * @autor d.snytko
 */
interface Presenter<VIEW : BaseView<NetworkState<*>>> {

    fun attach(view: VIEW)

    fun detach()
}
