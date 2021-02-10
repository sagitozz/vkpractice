package com.vkpractice.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vkpractice.app.data.database.converters.CareerConverter
import com.vkpractice.app.data.database.dao.CacheTimeStampDao
import com.vkpractice.app.data.database.dao.CommentsDao
import com.vkpractice.app.data.database.dao.PostsDao
import com.vkpractice.app.data.database.dao.ProfileDao
import com.vkpractice.app.data.database.entities.CacheTimeStamp
import com.vkpractice.app.data.database.entities.CommentsModel
import com.vkpractice.app.data.database.entities.PostModel
import com.vkpractice.app.data.database.entities.ProfileModel

/**
 * @autor d.snytko
 */
@Database(
    entities = [
        PostModel::class,
        CacheTimeStamp::class,
        CommentsModel::class,
        ProfileModel::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(CareerConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPostsDao(): PostsDao

    abstract fun getCacheTimeStampDao(): CacheTimeStampDao

    abstract fun getCommentsDao(): CommentsDao

    abstract fun getProfileDao(): ProfileDao
}


