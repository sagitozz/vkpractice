package com.vkpractice.app.ui.favoriteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.vkpractice.app.R
import com.vkpractice.app.application.App
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseFragment
import com.vkpractice.app.ui.detailsFragment.DetailsFragment
import com.vkpractice.app.ui.newsFragment.PostAdapter
import com.vkpractice.app.ui.newsFragment.PostDelegate
import com.vkpractice.app.ui.recycler.ItemTouchHelperAdapter
import com.vkpractice.app.ui.recycler.ItemTouchHelperCallback
import com.vkpractice.app.ui.recycler.itemdecoration.CustomDivider
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import moxy.ktx.moxyPresenter

/**
 * @autor d.snytko
 */
class FavoriteFragment : BaseFragment(R.layout.fragment_favorite), FavoritePageContract.View,
    PostDelegate {

    private val presenter by moxyPresenter { App.appComponent.getFavoritePresenter() }
    private val adapter = PostAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackPressCallback()
        initRefreshListener()
        initRecycler()
        presenter.attach(this)
        presenter.loadData()
    }
    override fun initRecycler() {
        favoriteRecycler.adapter = adapter
        val dateDivider = LayoutInflater.from(requireContext()).inflate(R.layout.divider_layout, favoriteRecycler, false) as TextView
        val itemDividerItemDecoration = CustomDivider(dateDivider, adapter)
        favoriteRecycler.addItemDecoration(itemDividerItemDecoration)

        val callback = ItemTouchHelperCallback(adapter as ItemTouchHelperAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(favoriteRecycler)
    }

    override fun initRefreshListener() {
        swipeRefreshLayoutFavorite.setOnRefreshListener {
            presenter.loadData()
        }
    }

    override fun showIdle(show: Boolean) {
        swipeRefreshLayoutFavorite.isRefreshing = show
    }

    override fun onError(error: Throwable?) {
        showIdle(false)
        error_layout.isVisible = true
        error_layout.retryButton.setOnClickListener { presenter.loadData() }
        showErrorToast(error)
        navBarHandler.navBarSetVisible(false)
    }

    override fun onStateReceive(state: NetworkState<List<PostModel>>) {
        when (state) {
            is NetworkState.ListLoaded -> onDataReceive(state.data)
            is NetworkState.Loading -> showIdle(true)
            is NetworkState.Error -> showIdle(false)
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

    override fun updateAdapter(favorites: List<PostModel>) {
        adapter.update(favorites)
    }

    override fun navigateToNewsFragment() {
        navBarHandler.navigateFromFavorite()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    private fun onDataReceive(postsList: List<PostModel>) {
        showIdle(false)
        navBarHandler.navBarSetVisible(true)
        updateAdapter(postsList)
        error_layout.isVisible = false
    }

    private fun initBackPressCallback() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun openDetailsFragment(postData: PostModel) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.enter_out_top, R.animator.exit_in_top,
                R.animator.enter_out_bottom, R.animator.exit_in_bottom
            )
            .replace(R.id.fragmentContainer, DetailsFragment.newInstance(postData))
            .addToBackStack(null)
            .commit()
    }

    companion object {

        fun newInstance(): Fragment {
            return FavoriteFragment()
        }
    }
}
