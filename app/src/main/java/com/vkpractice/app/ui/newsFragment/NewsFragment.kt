package com.vkpractice.app.ui.newsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.vkpractice.app.R
import com.vkpractice.app.application.App
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseFragment
import com.vkpractice.app.ui.detailsFragment.DetailsFragment
import com.vkpractice.app.ui.recycler.ItemTouchHelperAdapter
import com.vkpractice.app.ui.recycler.ItemTouchHelperCallback
import com.vkpractice.app.ui.recycler.itemdecoration.CustomDivider
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.posts_shimmer_layout.*
import moxy.ktx.moxyPresenter

/**
 * @autor d.snytko
 */
class NewsFragment : BaseFragment(R.layout.fragment_news), NewsPageContract.View,
    PostDelegate {

    private val presenter by moxyPresenter { App.appComponent.getNewsPresenter() }
    private val adapter = PostAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        initRecycler()
        initRefreshListener()
        presenter.loadDataFromRepository()
        presenter.checkForFavorites()
    }

    override fun initRecycler() {
        postRecycler.adapter = adapter
        val dateDivider =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.divider_layout, postRecycler, false) as TextView
        postRecycler.addItemDecoration(CustomDivider(dateDivider, adapter))

        val callback = ItemTouchHelperCallback(adapter as ItemTouchHelperAdapter)
        ItemTouchHelper(callback).attachToRecyclerView(postRecycler)
    }

    override fun onError(error: Throwable?) {
        showIdle(false)
        error_layout.isVisible = true
        error_layout.retryButton.setOnClickListener {
            presenter.loadDataFromNetwork()
            presenter.checkForFavorites()
        }
        showErrorToast(error)
        navBarHandler.navBarSetVisible(false)
    }

    override fun favoriteIconShow(show: Boolean) {
        navBarHandler.favoriteIconShow(show)
    }

    override fun initRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            adapter.newsList = emptyList()
            presenter.loadDataFromNetwork()
            presenter.checkForFavorites()
        }
    }

    override fun updateAdapter(posts: List<PostModel>) {
        adapter.update(posts)
    }

    override fun onStateReceive(state: NetworkState<List<PostModel>>) {
        when (state) {
            is NetworkState.ListLoaded -> onDataReceive(state.data)
            is NetworkState.Error -> onError(state.error)
            is NetworkState.Loading -> showIdle(true)
        }
    }

    override fun onItemClick(postData: PostModel) {
        openDetailsFragment(postData)
    }

    override fun onClickShare(post: PostModel) {
        imageHelper.sharePhoto(post.postImage)
    }

    override fun onImageClick(post: PostModel): Boolean {
        imageHelper.savePhotoFile(post.postImage)
        return true
    }

    override fun onItemRemove(post: PostModel) {
        presenter.removeItem(post)
    }

    override fun onLikeItem(post: PostModel) {
        presenter.likeItem(post)
    }

    override fun showIdle(show: Boolean) {
        if (show) {
            posts_shimmer_frame_layout.isVisible = true
            postRecycler.isVisible = !show
            posts_shimmer_frame_layout.startShimmer()
            swipeRefreshLayout.isRefreshing = !show
        } else {
            posts_shimmer_frame_layout.stopShimmer()
            posts_shimmer_frame_layout.isVisible = false
            postRecycler.isVisible = !show
        }
    }

    private fun onDataReceive(postsList: List<PostModel>) {
        showIdle(false)
        navBarHandler.navBarSetVisible(true)
        updateAdapter(postsList)
        error_layout.isVisible = false
    }

    private fun openDetailsFragment(postData: PostModel) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.enter_out_top, R.animator.exit_in_top,
                R.animator.enter_out_bottom, R.animator.exit_in_bottom
            )
            .replace(R.id.fragmentContainer, DetailsFragment.newInstance(postData))
            .addToBackStack("transactionID")
            .commit()
    }

    companion object {

        private const val WRITE_EXTERNAL_PERMISSION_CODE = 1

        fun newInstance(): Fragment {
            return NewsFragment()
        }
    }
}
