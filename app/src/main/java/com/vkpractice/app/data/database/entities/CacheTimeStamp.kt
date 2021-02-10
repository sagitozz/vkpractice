package com.vkpractice.app.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @autor d.snytko
 */
@Entity(tableName = "cache_timestamp_table")
class CacheTimeStamp(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int = 1,

    @ColumnInfo(name = "timeStamp")
    val timeStamp: Long = 0L
)
