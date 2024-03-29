package com.app.runtogether.db.runToUser

import androidx.room.*
import com.app.runtogether.db.run.Run
import com.app.runtogether.db.user.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RunWithUsersDao {

    @Query("SELECT EXISTS (SELECT 1 FROM RunUserCrossRef WHERE run_Id = :runId AND user_Id = :userId)")
    fun doesRunUserCrossRefExist(runId: Int, userId: Int): Boolean

    @Transaction
    @Query("SELECT * FROM RunUserCrossRef as ru, User as u where ru.run_id = :runId and ru.user_id = u.user_id")
    fun getUsersFromRun(runId: Int): Flow<List<User>>

    @Transaction
    @Query("SELECT count(*) FROM RunUserCrossRef group by user_id having user_id = :userId")
    fun getNumberOfRunsJoined(userId: Int): Flow<Int>

    @Transaction
    @Query("SELECT * FROM RunUserCrossRef as ru, Run as r where ru.user_id = :userId and ru.run_id = r.run_id")
    fun getAllRunsFromUserId(userId: Int): Flow<List<Run>>

    @Insert
    suspend fun insertRunUserCrossRef(runUserCrossRef: RunUserCrossRef)

    @Query("SELECT run_id FROM RunUserCrossRef where user_id = :userId")
    fun getRunsIdFromUserId(userId: Int): Flow<List<Int>>

    @Delete
    fun deleteFromDb(runUserCrossRef: RunUserCrossRef)
}