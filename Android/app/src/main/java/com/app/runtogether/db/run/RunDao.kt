package com.app.runtogether.db.run

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
//get runs from city
    @Query("SELECT * FROM Run WHERE city = :city")
    fun getRunsFromCity(city: String): Flow<List<Run>>

    @Insert
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)
}