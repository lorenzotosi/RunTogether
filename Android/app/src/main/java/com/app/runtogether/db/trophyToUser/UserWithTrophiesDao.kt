package com.app.runtogether.db.trophyToUser

import androidx.room.*
import com.app.runtogether.db.trophy.Trophy
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWithTrophiesDao {
    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithTrophies(): Flow<List<UserWithTrophies>>

    @Transaction
    @Query("SELECT * FROM Trophy WHERE trophy_id NOT IN (SELECT trophy_id FROM TrophyUserCrossRef WHERE user_id = :userId)")
    fun getTrophyNotHave(userId: Int): Flow<List<Trophy>>

    //get trophies that user have
    @Transaction
    @Query("SELECT * FROM Trophy WHERE trophy_id IN (SELECT trophy_id FROM TrophyUserCrossRef WHERE user_id = :userId)")
    fun getTrophyHave(userId: Int): Flow<List<Trophy>>

    @Transaction
    @Query("SELECT * FROM TrophyUserCrossRef WHERE trophy_id = :trophyId and user_id = :userId")
    fun hasUserGotTrophy(userId: Int, trophyId: Int): Flow<Boolean>

    @Transaction
    @Query("SELECT COUNT(trophy_id) FROM TrophyUserCrossRef WHERE user_id = :userId")
    fun getNumberOfTrophies(userId: Int): Flow<Int>

    @Insert
    suspend fun insertTrophyUserCrossRef(trophyUserCrossRef: TrophyUserCrossRef)

}