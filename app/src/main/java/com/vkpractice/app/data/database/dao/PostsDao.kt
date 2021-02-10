package com.vkpractice.app.data.database.dao

import androidx.room.*
import com.vkpractice.app.data.database.entities.PostModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @autor d.snytko
 */
@Dao
interface PostsDao {

    @Insert
    fun insert(vkItemEntity: PostModel): Completable

    @Update
    fun update(vkItemEntity: PostModel): Completable

    @Delete
    fun delete(vkItemEntity: PostModel): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<PostModel>) : Completable

    @Query("DELETE FROM vk_item_table WHERE wallPost = 0")
    fun deleteAllNewsPosts(): Completable

    @Query("DELETE FROM vk_item_table WHERE wallPost = 1")
    fun deleteAllWallPosts(): Completable

    @Query("SELECT * FROM vk_item_table  WHERE wallPost = 0 ORDER BY postDate DESC")
    fun getAllNewsPosts(): Single<List<PostModel>>

    @Query("SELECT * FROM vk_item_table WHERE wallPost = 1 ORDER BY postDate DESC")
    fun getAllWallPosts(): Single<List<PostModel>>

    @Query("SELECT COUNT() FROM vk_item_table WHERE wallPost == 1")
    fun wallPostCount(): Single<Int>

    @Query("SELECT * FROM vk_item_table ORDER BY postDate DESC")
    fun getAllPosts(): Single<List<PostModel>>

    @Query("SELECT * FROM vk_item_table LIMIT 1")
    fun checkIfPostSavedExists(): Single<List<PostModel>>
}
