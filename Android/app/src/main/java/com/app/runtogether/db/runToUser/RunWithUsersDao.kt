package com.app.runtogether.db.runToUser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.app.runtogether.db.run.Run
import kotlinx.coroutines.flow.Flow

@Dao
interface RunWithUsersDao {

    @Transaction
    @Query("SELECT * FROM User, RunUserCrossRef WHERE User.user_id = RunUserCrossRef.user_id and RunUserCrossRef.run_id = :runId")
    fun getUsersFromRun(runId: Int): Flow<List<RunWithUsers>>

    @Transaction
    @Query("SELECT count(*) FROM RunUserCrossRef group by user_id having user_id = :userId")
    fun getNumberOfRunsJoined(userId: Int): Flow<Int>

    @Transaction
    @Query("SELECT * FROM RunUserCrossRef as ru, Run as r where ru.user_id = :userId and ru.run_id = r.run_id")
    fun getAllRunsFromUserId(userId: Int): Flow<List<Run>>

    @Insert
    suspend fun insertRunUserCrossRef(runUserCrossRef: RunUserCrossRef)
}