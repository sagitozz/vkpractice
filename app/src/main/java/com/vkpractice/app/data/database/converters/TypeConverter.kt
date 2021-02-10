package com.vkpractice.app.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * @autor d.snytko
 */
interface TypeConverter<T> {

    val clazz: Type

    @TypeConverter
    fun fromType(model: T): String =
        Gson().toJson(model)

    @TypeConverter
    fun fromString(value: String): T {
        return Gson().fromJson(value, clazz)
    }
}
