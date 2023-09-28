package com.app.runtogether.db.runToUser

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RunWithUsersDao {
    @Transaction
    @Query("SELECT * FROM Run")
    fun getUserFromRun(): Flow<List<RunWithUsers>>

}