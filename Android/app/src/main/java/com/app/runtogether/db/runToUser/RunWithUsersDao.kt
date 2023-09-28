package com.app.runtogether.db.runToUser

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RunWithUsersDao {
    @Transaction
    @Query("SELECT * FROM Run")
    fun getUserAndRuns(): Flow<List<RunWithUsers>>

    @Transaction
    @Query("SELECT * FROM User, RunUserCrossRef WHERE User.user_id = RunUserCrossRef.user_id and RunUserCrossRef.run_id = :runId")
    fun getUsersFromRun(runId: Int): Flow<List<RunWithUsers>>

}