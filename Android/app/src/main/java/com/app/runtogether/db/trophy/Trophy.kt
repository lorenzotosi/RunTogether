package com.app.runtogether.db.trophy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Trophy")
data class Trophy(
    @PrimaryKey(autoGenerate = true) val trophy_id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "path") val path: Int?,
    @ColumnInfo(name = "km") val km: Int?
)