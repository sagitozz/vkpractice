package com.vkpractice.app.ui.recycler.itemdecoration

/**
 * @autor d.snytko
 */
interface DateCallbackAdapter {

    fun elementHasDateHeader(position : Int) : Boolean

    fun getDateHeaderText(position: Int) : String
}
