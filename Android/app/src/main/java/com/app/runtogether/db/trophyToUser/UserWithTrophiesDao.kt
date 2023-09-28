package com.app.runtogether.db.trophyToUser

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWithTrophiesDao {
    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithTrophies(): Flow<List<UserWithTrophies>>

    @Transaction
    @Query("SELECT COUNT(trophy_id) FROM TrophyUserCrossRef WHERE user_id = :userId")
    fun getNumberOfTrophies(userId: Int): Flow<Int>

    @Insert
    suspend fun insertTrophyUserCrossRef(trophyUserCrossRef: TrophyUserCrossRef)


}