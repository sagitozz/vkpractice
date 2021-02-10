package com.vkpractice.app.ui.profileFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.ui.recycler.ProfileDiffUtilsCallback
import com.vkpractice.app.utils.DateHelper
import com.vkpractice.app.utils.ImageLoader
import kotlinx.android.synthetic.main.profile_layout_item.view.*

/**
 * @autor d.snytko
 */
class ProfileAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, ProfileDiffUtilsCallback())

    var newsList: List<ProfileModel> = listOf()
        set(value) {
            differ.submitList(value)
            field = value
        }
        get() = differ.currentList

    fun update(dataList: List<ProfileModel>) {
        newsList = dataList.toMutableList()
    }

    private class ProfileVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProfileModel) {
            with(itemView) {
                ImageLoader.loadImage(item.photoMaxOrig, profileAvatar)
                profileName.text = context.getString(R.string.commenter_name, item.firstName, item.lastName)
                lastSeenStatus.text = context.getString(R.string.profile_last_seen, DateHelper.convertLongToTime(item.lastSeen))
                idAddress.text = context.getString(R.string.profile_id, item.id.toString())
                city.text = item.city
                country.text = item.country
                friends.text = context.getString(R.string.profile_friends)
                groups.text = context.getString(R.string.profile_groups)
                wall.text = context.getString(R.string.profile_wall)
                birthday.text = context.getString(R.string.profile_bday, item.birthday)
                checkAboutExists(item)
                checkEducationExists(item)
                wallCount.text = item.postCount.toString()
                friendsCount.text = item.friendsCount.toString()
                groupsCount.text = item.groupsCount.toString()
                if (item.profileCareer.isNotEmpty()) {
                    career.text = context.getString(R.string.profile_work_field)
                } else {
                    career.isVisible = false
                }
            }
        }

        private fun checkEducationExists(item: ProfileModel) {
            if (item.universityName?.isNotEmpty()!!) {
                itemView.education.text = itemView.context.getString(
                    R.string.profile_education,
                    item.universityName,
                    item.facultyName,
                    item.graduation
                )
            } else {
                itemView.education.isVisible = false
                itemView.secondDivider.isVisible = false
            }
        }

        private fun checkAboutExists(item: ProfileModel) {
            if (item.about?.isNotEmpty()!!) {
                itemView.about.text = itemView.context.getString(R.string.profile_about, item.about)
            } else {
                itemView.about.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProfileVH(LayoutInflater.from(parent.context).inflate(R.layout.profile_layout_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ProfileVH).bind(newsList[position])

    override fun getItemCount(): Int = newsList.size
}
