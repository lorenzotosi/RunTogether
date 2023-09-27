package com.app.runtogether.db.trophyToUser

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserWithTrophiesDao {
    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithTrophies(): List<UserWithTrophies>
}