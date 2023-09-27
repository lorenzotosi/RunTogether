package com.app.runtogether.db.run_to_user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.app.runtogether.db.run.Run
import com.app.runtogether.db.user.User

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Run::class,
            parentColumns = ["id"],
            childColumns = ["trophy_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Run_to_user(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val user_id: Int,
    val trophy_id: Int
)
