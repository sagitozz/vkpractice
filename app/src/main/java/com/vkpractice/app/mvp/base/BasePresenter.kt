package com.vkpractice.app.mvp.base

import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.Presenter
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter

/**
 * @autor d.snytko
 */
abstract class BasePresenter<VIEW : BaseView<NetworkState<*>>> : Presenter<VIEW>,
    MvpPresenter<VIEW>() {

    protected val presenterDisposable: CompositeDisposable = CompositeDisposable()

    var view: VIEW? = null

    override fun attach(view: VIEW) {
        this.view = view
    }

    override fun detach() {
        presenterDisposable.clear()
        view = null
    }
}
