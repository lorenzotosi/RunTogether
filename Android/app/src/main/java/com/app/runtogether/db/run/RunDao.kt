package com.app.runtogether.db.run

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
//get runs from city
    @Query("SELECT * FROM Run WHERE city = :city and organized = true")
    fun getRunsFromCity(city: String): Flow<List<Run>>

    @Query("SELECT * FROM run WHERE city = :city AND day BETWEEN :startOfDay AND :endOfDay")
    fun getRunsFromCityForToday(city: String, startOfDay: Long, endOfDay: Long): Flow<List<Run>>
    @Insert
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT polyline FROM Run ORDER BY run_id DESC LIMIT 1")
    fun getOnlyPolyFromId(): Flow<String>

    @Query("SELECT * FROM Run ORDER BY run_id DESC LIMIT 1")
    fun getLastRunDistance(): Flow<Run>

    @Query("SELECT * FROM Run where run_id = :id")
    fun getRunFromId(id : Int): Flow<Run>
}