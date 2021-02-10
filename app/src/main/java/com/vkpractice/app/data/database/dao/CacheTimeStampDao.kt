package com.vkpractice.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vkpractice.app.data.database.entities.CacheTimeStamp
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @autor d.snytko
 */
@Dao
interface CacheTimeStampDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cacheTimeStamp: CacheTimeStamp): Completable

    @Query("SELECT * FROM cache_timestamp_table")
    fun getCacheTimeStamp(): Single<CacheTimeStamp>

    @Query("SELECT COUNT() FROM cache_timestamp_table")
    fun count(): Single<Int>
}
