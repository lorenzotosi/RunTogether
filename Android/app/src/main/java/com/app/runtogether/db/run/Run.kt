package com.app.runtogether.db.run

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Run")
data class Run(
    @PrimaryKey(autoGenerate = true) val run_id: Int = 0,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "length_km") val length_km: Int?,
    @ColumnInfo(name="day") val day: Long?,
    @ColumnInfo(name = "polyline") val polyline : String?
)