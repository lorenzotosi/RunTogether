package com.app.runtogether.db.favorite

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

interface FavoriteTrophyUserDao {
    @Transaction
    @Query("SELECT * FROM FavoriteTrophyUser WHERE user_id = :userId")
    fun getFavoriteTrophyUser(userId: Int): List<FavoriteTrophyUser>

    @Transaction
    @Query("SELECT * FROM FavoriteTrophyUser WHERE user_id = :userId AND trophy_id = :trophyId")
    fun getFavoriteTrophyUser(userId: Int, trophyId: Int): FavoriteTrophyUser

    @Insert
    suspend fun insertFavoriteTrophyUser(favoriteTrophyUser: Favorite)
}