package com.app.runtogether.db.notify

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notify")
data class Notify(
    @PrimaryKey(autoGenerate = true) val notify_id: Int = 0,
    @ColumnInfo(name = "uid_sent") val uid_sent: Int?,
    @ColumnInfo(name = "uid_received") val uid_received: Int?,
    @ColumnInfo(name = "text") val text: String?
)