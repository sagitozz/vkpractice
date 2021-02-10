package com.vkpractice.app.data.database.dao

import androidx.room.*
import com.vkpractice.app.data.database.entities.ProfileModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @autor d.snytko
 */
@Dao
interface ProfileDao {

    @Insert
    fun insert(commentsEntity: ProfileModel): Completable

    @Update
    fun update(commentsEntity: ProfileModel): Completable

    @Delete
    fun delete(commentsEntity: ProfileModel): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(commentsEntities: List<ProfileModel>): Completable

    @Query("DELETE FROM profile_table")
    fun deleteProfile(): Completable

    @Query("SELECT * FROM profile_table")
    fun getProfile(): Single<List<ProfileModel>>

    @Query("SELECT COUNT() FROM profile_table")
    fun count(): Single<Int>
}
