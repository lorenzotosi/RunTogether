package com.app.runtogether.db.trophyToUser

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWithTrophiesDao {
    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithTrophies(): Flow<List<UserWithTrophies>>

    @Transaction
    @Query("SELECT COUNT(trophy_id) FROM TrophyUserCrossRef, User WHERE User.user_id = TrophyUserCrossRef.user_id and User.username = :userId")
    fun getNumberOfTrophies(userId: String): Flow<Int>
}