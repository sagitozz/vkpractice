package com.vkpractice.app.data.database.dao

import androidx.room.*
import com.vkpractice.app.data.database.entities.CommentsModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @autor d.snytko
 */
@Dao
interface CommentsDao {

    @Insert
    fun insert(commentsEntity: CommentsModel): Completable

    @Update
    fun update(commentsEntity: CommentsModel): Completable

    @Delete
    fun delete(commentsEntity: CommentsModel): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(commentsEntities: List<CommentsModel>): Completable

    @Query("DELETE FROM comments_table")
    fun deleteAllComments(): Completable

    @Query("SELECT * FROM comments_table WHERE postId = :postId ORDER BY date ASC")
    fun getCommentsToPost(postId: Int?): Single<List<CommentsModel>>

    @Query("SELECT COUNT() FROM comments_table")
    fun count(): Single<Int>
}
