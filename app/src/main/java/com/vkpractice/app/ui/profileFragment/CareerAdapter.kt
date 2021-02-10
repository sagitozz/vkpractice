package com.vkpractice.app.ui.profileFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.vkpractice.app.R
import com.vkpractice.app.data.network.responses.CareerItem
import com.vkpractice.app.ui.recycler.CareerDiffUtilsCallback
import com.vkpractice.app.utils.DateHelper
import com.vkpractice.app.utils.ImageLoader
import kotlinx.android.synthetic.main.career_item.view.*

/**
 * @autor d.snytko
 */
class CareerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, CareerDiffUtilsCallback())

    var newsList: List<CareerItem?>? = listOf()
        set(value) {
            differ.submitList(value)
            field = value
        }
        get() = differ.currentList

    fun update(dataList: List<CareerItem?>?) {
        newsList = dataList?.toMutableList()
    }

    private class CareerVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(item: CareerItem?) {
            with(itemView) {
                ImageLoader.loadAvatarImage(item?.groupPhoto, companyAvatar)
                companyName.text = item?.groupName
                if (item?.from != FROM_NOTHING) {
                    startPeriod.text = context.getString(R.string.career_period, item?.from, DateHelper.compareCareerDates(item?.from, item?.untilDate))
                } else startPeriod.text = context.getString(R.string.carrer_until, DateHelper.compareCareerDates(item.from, item.untilDate))
                workPosition.text = item?.position
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CareerVH(LayoutInflater.from(parent.context).inflate(R.layout.career_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as CareerVH).bind(newsList!![position])


    override fun getItemCount(): Int = newsList!!.size

    private companion object {

        const val FROM_NOTHING = 0
    }
}
