package com.app.runtogether.db.run

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val name: Date?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "city") val path: String?,
    @ColumnInfo(name = "lengthKm") val lengthKm: Int?
)