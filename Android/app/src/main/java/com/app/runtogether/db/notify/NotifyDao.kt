package com.app.runtogether.db.notify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifyDao {

    @Insert
    suspend fun insert(notify: Notify)

    @Delete
    suspend fun delete(notify: Notify)

    @Query("SELECT text FROM Notify WHERE uid_received = :uid_received")
    fun findByUid(uid_received: Int): Flow<List<String>>


}