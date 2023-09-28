package com.app.runtogether.db.polylines

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PolylineDao {
    @Insert
    suspend fun insert(polyline: PolylineEntity)

    @Query("SELECT * FROM polylines")
    fun getAllPolylines(): Flow<List<PolylineEntity>>

    @Query("SELECT points FROM polylines where id = 1")
    fun getOnlyPoly(): Flow<String>
}
