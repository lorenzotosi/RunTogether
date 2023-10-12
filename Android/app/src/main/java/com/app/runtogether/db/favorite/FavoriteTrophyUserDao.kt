package com.app.runtogether.db.favorite

import androidx.room.*
import com.app.runtogether.db.trophy.Trophy
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrophyUserDao {

    @Transaction
    @Query("SELECT * FROM Trophy WHERE trophy_id IN (SELECT trophy_id FROM Favorite WHERE user_id = :userId) and trophy_id NOT IN (SELECT trophy_id FROM TrophyUserCrossRef WHERE user_id = :userId)")
    fun getFavoriteTrophyUser(userId: Int): Flow<List<Trophy>>

    @Transaction
    @Query("SELECT * FROM Trophy WHERE trophy_id NOT IN (SELECT trophy_id FROM Favorite WHERE user_id = :userId) and trophy_id NOT IN (SELECT trophy_id FROM TrophyUserCrossRef WHERE user_id = :userId)")
    fun getNotFavoriteTrophyUser(userId: Int): Flow<List<Trophy>>

    @Transaction
    @Query("SELECT count(*) FROM Favorite WHERE user_id = :userId AND trophy_id = :trophyId group by trophy_id")
    fun getFavoriteTrophyUser(userId: Int, trophyId: Int): Flow<Int>

    @Insert
    suspend fun insertFavoriteTrophyUser(favoriteTrophyUser: Favorite)

    @Delete
    suspend fun deleteFavoriteTrophyUser(favoriteTrophyUser: Favorite)
}