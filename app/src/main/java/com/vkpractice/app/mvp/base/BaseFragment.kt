package com.vkpractice.app.mvp.base

import android.content.Context
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.vkpractice.app.R
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.utils.ImageHelper
import com.vkpractice.app.utils.NavBarHandler
import moxy.MvpAppCompatFragment

/**
 * @autor d.snytko
 */
abstract class BaseFragment(@LayoutRes private val contentLayoutId: Int) :
    MvpAppCompatFragment(contentLayoutId),
    BaseView<NetworkState<*>> {

    lateinit var imageHelper: ImageHelper
    lateinit var navBarHandler: NavBarHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ImageHelper) {
            imageHelper = context
        } else {
            throw IllegalStateException(getString(R.string.activity_must_implement_imagehelper))
        }
        if (context is NavBarHandler) {
            navBarHandler = context
        } else {
            throw IllegalStateException(getString(R.string.activity_must_implement_navbarhandler))
        }
    }

    abstract fun initRefreshListener()

    abstract fun initRecycler()

    fun showErrorToast(error: Throwable?) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.error_toast, error?.localizedMessage),
            Toast.LENGTH_LONG
        ).show()
    }
}
