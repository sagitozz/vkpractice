package com.vkpractice.app.ui.detailsFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.application.App
import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.mvp.NetworkState
import com.vkpractice.app.mvp.base.BaseFragment
import com.vkpractice.app.ui.recycler.ItemTouchHelperAdapter
import com.vkpractice.app.ui.recycler.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.posts_shimmer_layout.*
import moxy.ktx.moxyPresenter

/**
 * @autor d.snytko
 */
class DetailsFragment : BaseFragment(R.layout.fragment_details), DetailsPageContract.View,
    DetailsPostDelegate {

    private val presenter by moxyPresenter { App.appComponent.getDetailsPresenter() }
    private val commentsAdapter = CommentsAdapter()
    private val detailPostAdapter = DetailPostAdapter(this)
    private val concatAdapter = ConcatAdapter(detailPostAdapter, commentsAdapter)

    private lateinit var receivedMessage: PostModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRefreshListener()
        initEditTextAndButtonListeners()
        initRecycler()
        navBarHandler.navBarSetVisible(false)
        receivedMessage = requireArguments().getParcelable(POST_ITEM)!!
        checkIfCanComment()
        presenter.loadComments(receivedMessage)
    }

    override fun initRefreshListener() {
        swipeRefreshLayoutDetails.setOnRefreshListener {
            presenter.loadComments(receivedMessage)
        }
        navBarHandler.navBarSetVisible(false)
    }

    override fun initRecycler() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        commentsRecycler.adapter = concatAdapter
        commentsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy != 0) {
                    editCommentText.clearFocus()
                    inputMethodManager?.hideSoftInputFromWindow(
                        editCommentText.applicationWindowToken,
                        InputMethodManager.RESULT_UNCHANGED_SHOWN
                    )
                }
            }
        })
        val callback = ItemTouchHelperCallback(detailPostAdapter as ItemTouchHelperAdapter)
        ItemTouchHelper(callback).attachToRecyclerView(commentsRecycler)
    }

    override fun updateAdapter(post: PostModel, comments: List<CommentsModel>) {
        detailPostAdapter.update(listOf(post))
        if (comments.isNotEmpty())
            commentsAdapter.update(comments)
        else {
            commentsAdapter.update(listOf(CommentsModel(id = -1)))
        }
    }

    override fun onStateReceive(state: NetworkState<List<CommentsModel>>) {
        when (state) {
            is NetworkState.ListLoaded -> onDataReceive(state.data)
            is NetworkState.Error -> onError(state.error)
            is NetworkState.Loading -> showIdle(true)
        }
    }

    override fun showIdle(show: Boolean) {
        if (show) {
            posts_shimmer_frame_layout.visibility = View.VISIBLE
            commentsRecycler.isVisible = !show
            posts_shimmer_frame_layout.startShimmer()
            swipeRefreshLayoutDetails.isRefreshing = !show
        } else {
            posts_shimmer_frame_layout.stopShimmer()
            posts_shimmer_frame_layout.visibility = View.GONE
            commentsRecycler.isVisible = !show
        }
    }

    override fun onError(error: Throwable?) {
        error_layout.isVisible = true
        error_layout.retryButton.setOnClickListener { presenter.loadComments(receivedMessage) }
        showErrorToast(error)
    }

    override fun onClickShare(post: PostModel) {
        imageHelper.sharePhoto(post.postImage)
    }

    override fun onPressLike(post: PostModel) {
        presenter.likeItem(post)
    }

    override fun onImageClick(post: PostModel) : Boolean{
        imageHelper.savePhotoFile(post.postImage)
        return true
    }

    private fun onDataReceive(comments: List<CommentsModel>) {
        showIdle(false)
        updateAdapter(receivedMessage, comments)
        error_layout.isVisible = false
    }

    private fun checkIfCanComment() {
        if (receivedMessage.canPost == CANT_POST) {
            editCommentText.isVisible = false
            leaveCommentButton.isVisible = false
        } else {
            editCommentText.isVisible = true
            leaveCommentButton.isVisible = true
        }
    }

    private fun initEditTextAndButtonListeners() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        editCommentText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val commentMessage = editCommentText.text.toString()
                presenter.sendComment(receivedMessage, commentMessage)
                editCommentText.text.clear()
                editCommentText.clearFocus()
                inputMethodManager?.hideSoftInputFromWindow(v.applicationWindowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                return@setOnEditorActionListener true
            }
            false
        }

        leaveCommentButton.setOnClickListener {
            val commentMessage = editCommentText.text.toString()
            presenter.sendComment(receivedMessage, commentMessage)
            editCommentText.text.clear()
            inputMethodManager?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
            editCommentText.clearFocus()
        }
    }

    companion object {

        private const val CANT_POST = 0

        private const val POST_ITEM = "post_item"

        fun newInstance(postData: PostModel): Fragment = DetailsFragment().apply {
            arguments = bundleOf(POST_ITEM to postData)
        }
    }
}
