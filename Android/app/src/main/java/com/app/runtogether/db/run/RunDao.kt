package com.app.runtogether.db.run

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RunDao {

    @Insert
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM Run")
    suspend fun getAllRuns(): List<Run>

    @Query("SELECT * FROM Run WHERE run_id = :runId")
    suspend fun getRunById(runId: Int): Run?

    @Query("SELECT * FROM Run WHERE city = :city")
    suspend fun getRunByCity(city: String): Run?
}