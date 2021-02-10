package com.vkpractice.app.data.database.converters

import com.google.gson.reflect.TypeToken
import com.vkpractice.app.data.network.responses.CareerItem
import java.lang.reflect.Type

/**
 * @autor d.snytko
 */
class CareerConverter : TypeConverter<List<CareerItem>> {

    override val clazz: Type
        get() = object : TypeToken<List<CareerItem>>() {}.type
}
