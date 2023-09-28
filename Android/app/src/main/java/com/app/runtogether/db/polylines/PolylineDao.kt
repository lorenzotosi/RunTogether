package com.app.runtogether.db.polylines

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PolylineDao {
    @Insert
    suspend fun insert(polyline: PolylineEntity)

    @Query("SELECT * FROM polylines")
    suspend fun getAllPolylines(): List<PolylineEntity>
}
