package com.vkpractice.app.utils

import com.vkpractice.app.data.database.entities.PostModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * @autor d.snytko
 */
object DateHelper {

    private val locale = Locale("ru")
    private const val ONE_DAY = 1
    private const val TILL_NOW = "наст. время"
    private const val YESTERDAY = "Вчера"
    private const val TODAY = "Сегодня"


    fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    fun compareCareerDates(from : Int?, until : Int?) : String {
        return if (from == until) {
            TILL_NOW
        } else until.toString()
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val calendarDate = Calendar.getInstance(locale)
        calendarDate.timeInMillis = System.currentTimeMillis()
        val currDate = calendarDate.get(Calendar.DAY_OF_YEAR)
        val prevDate = calendarDate.get(Calendar.DAY_OF_YEAR) - ONE_DAY
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return when (calendarDate.get(Calendar.DAY_OF_YEAR)) {
            prevDate -> dateFormat.format(date)
            currDate -> timeFormat.format(date)
            else -> dateFormat.format(date)
        }
    }

    fun isSameDays(first: Long, second: Long): Boolean {
        val firstCalendar = Calendar.getInstance(locale)
        val secondCalendar = Calendar.getInstance(locale)
        firstCalendar.timeInMillis = first
        secondCalendar.timeInMillis = second

        return firstCalendar.get(Calendar.DAY_OF_YEAR) != secondCalendar.get(Calendar.DAY_OF_YEAR)
    }

    fun setDateInDecorator(position: Int, postList: List<PostModel>): String {
        val postTimeMillis = postList[position].postDate
        val firstCalendar = Calendar.getInstance(locale)
        val secondCalendar = Calendar.getInstance(locale)
        secondCalendar.timeInMillis = postTimeMillis
        val date = Date(postTimeMillis)
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currDate = firstCalendar.get(Calendar.DAY_OF_YEAR)
        val prevDate = firstCalendar.get(Calendar.DAY_OF_YEAR) - ONE_DAY

        return when (secondCalendar.get(Calendar.DAY_OF_YEAR)) {
            prevDate -> YESTERDAY
            currDate -> TODAY
            else -> sdf.format(date)
        }
    }
}


