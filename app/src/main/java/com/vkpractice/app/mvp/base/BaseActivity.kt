package com.vkpractice.app.mvp.base

import android.widget.Toast
import androidx.annotation.LayoutRes
import com.vkpractice.app.R
import com.vkpractice.app.mvp.NetworkState
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpAppCompatActivity

/**
 * @autor d.snytko
 */
abstract class BaseActivity(@LayoutRes private val contentLayoutId: Int) : MvpAppCompatActivity(contentLayoutId), BaseView<NetworkState<*>> {

    protected var activityDisposable: CompositeDisposable = CompositeDisposable()

    abstract fun showError(error: Throwable?)

    fun showErrorToast(error: Throwable?) {
        Toast.makeText(
            applicationContext,
            applicationContext.getString(R.string.error_toast, error?.localizedMessage),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityDisposable.dispose()
    }
}
