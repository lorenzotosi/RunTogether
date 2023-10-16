package com.app.runtogether.db.run

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
//get runs from city
    @Query("SELECT * FROM Run WHERE city = :city and organized = true")
    fun getRunsFromCity(city: String): Flow<List<Run>>

    @Transaction
    @Query("SELECT Run.run_id, city, description, length_km, day, polyline, organized, startHour, endHour FROM Run, RunUserCrossRef as r WHERE r.run_id=Run.run_id and r.user_id = :id and Run.organized = false")
    fun getMyRuns(id: Int): Flow<List<Run>>

    @Query("SELECT * FROM run WHERE city = :city AND day BETWEEN :startOfDay AND :endOfDay AND organized = true")
    fun getRunsFromCityForToday(city: String, startOfDay: Long, endOfDay: Long): Flow<List<Run>>

    @Query("SELECT * FROM run WHERE city = :city AND day BETWEEN :startOfDay AND :endOfDay AND organized = true")
    fun getRunsFromCityForTodayNoFlow(city: String, startOfDay: Long, endOfDay: Long): List<Run>

    @Query("SELECT * FROM run WHERE city = :city AND day BETWEEN :startOfDay AND :endOfDay AND organized = true")
    suspend fun getRunsFromCityForTodayNoFlowS(city: String, startOfDay: Long, endOfDay: Long): List<Run>

    @Query("Select * from run where organized = true order by day asc")
    fun getAllOrganizedRuns(): List<Run>

    @Query("Select * from run where organized = true and day > :startOfDay order by day asc")
    fun getAllFutureOrganizedRuns(startOfDay: Long): List<Run>

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