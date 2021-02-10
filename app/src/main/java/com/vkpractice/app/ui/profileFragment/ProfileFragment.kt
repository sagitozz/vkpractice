package com.vkpractice.app.ui.profileFragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.application.App
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.data.network.responses.CareerItem
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
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_shimmer_layout.*
import moxy.ktx.moxyPresenter

/**
 * @autor d.snytko
 */
class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileFragmentContract.View,
    PostDelegate {

    private val presenter by moxyPresenter { App.appComponent.getProfilePresenter() }
    private val profileInfoAdapter = ProfileAdapter()
    private val careerAdapter = CareerAdapter()
    private val profilePostAdapter = PostAdapter(this)
    private val concatAdapter = ConcatAdapter(profileInfoAdapter, careerAdapter, profilePostAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshListener()
        initRecycler()
        initEditTextAndButtonListeners()
        presenter.loadData()
        presenter.checkForFavorites()
    }

    override fun initRecycler() {
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        profileRecycler.adapter = concatAdapter
        val dateDivider =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.divider_layout, profileRecycler, false) as TextView
        profileRecycler.addItemDecoration(CustomDivider(dateDivider, profilePostAdapter))

        val callback = ItemTouchHelperCallback(profilePostAdapter as ItemTouchHelperAdapter)
        ItemTouchHelper(callback).attachToRecyclerView(profileRecycler)

        profileRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy != POSITION_NULL ) {
                    clearFocusAndHideKeyboard(inputMethodManager)
                }
            }
        })
    }

    override fun initRefreshListener() {
        swipeRefreshLayoutProfile.setOnRefreshListener {
            presenter.loadData()
        }
    }

    override fun updateProfileAdapter(profile: List<ProfileModel>) {
        profileInfoAdapter.update(profile)
    }

    override fun updatePostAdapter(posts: List<PostModel>) {
        profilePostAdapter.update(posts)
        showIdle(false)
    }

    override fun updateCareerAdapter(careers: List<CareerItem>) {
        careerAdapter.update(careers)
    }

    override fun onStateReceive(state: NetworkState<Pair<List<ProfileModel>, List<PostModel>>>) {
        when (state) {
            is NetworkState.ListLoaded -> onDataReceive(state.data)
            is NetworkState.Error -> onError(state.error)
            is NetworkState.Loading -> showIdle(true)
        }
    }

    override fun showIdle(show: Boolean) {
        if (show) {
            profile_shimmer_frame_layout.isVisible = true
            profileRecycler.isVisible = !show
            profile_shimmer_frame_layout.startShimmer()
            swipeRefreshLayoutProfile.isRefreshing = !show
        } else {
            profile_shimmer_frame_layout.stopShimmer()
            profile_shimmer_frame_layout.isVisible = false
            profileRecycler.isVisible = !show
            swipeRefreshLayoutProfile.isRefreshing = show
        }
    }

    override fun favoriteIconShow(show: Boolean) {
        navBarHandler.favoriteIconShow(show)
    }

    override fun onError(error: Throwable?) {
        error_layout.isVisible = true
        error_layout.retryButton.setOnClickListener {
            presenter.loadData()
            presenter.checkForFavorites()
        }
        showErrorToast(error)
        navBarHandler.navBarSetVisible(false)
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
        presenter.likeItem(post)
    }

    override fun onLikeItem(post: PostModel) {
        presenter.likeItem(post)
    }

    private fun onDataReceive(data: Pair<List<ProfileModel>, List<PostModel>>) {
        navBarHandler.navBarSetVisible(true)
        updateProfileAdapter(data.first)
        updatePostAdapter(data.second)
        if (data.first.isNotEmpty()) updateCareerAdapter(data.first.first().profileCareer)
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

    private fun initEditTextAndButtonListeners() {
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

        writePostEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val post = writePostEditText.text.toString()
                presenter.sendPost(post)
                writePostEditText.text.clear()
                writePostEditText.clearFocus()
                inputMethodManager?.hideSoftInputFromWindow(v.applicationWindowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        sendPostButton.setOnClickListener {
            val post = writePostEditText.text.toString()
            presenter.sendPost(post)
            writePostEditText.text.clear()
            inputMethodManager?.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            writePostEditText.clearFocus()
        }
    }

    private fun clearFocusAndHideKeyboard(inputMethodManager: InputMethodManager?) {
        writePostEditText.clearFocus()
        inputMethodManager?.hideSoftInputFromWindow(
            writePostEditText.applicationWindowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    companion object {

        private const val POSITION_NULL = 0

        fun newInstance(): Fragment {
            return ProfileFragment()
        }
    }
}
