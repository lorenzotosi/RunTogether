package com.app.runtogether.db.run

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.app.runtogether.db.user.User
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_creator_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val name: Date?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "city") val path: String?,
    @ColumnInfo(name = "lengthKm") val lengthKm: Int?,
    val user_creator_id: Int
)