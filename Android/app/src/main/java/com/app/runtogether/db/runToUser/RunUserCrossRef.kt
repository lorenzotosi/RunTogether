package com.app.runtogether.db.runToUser

import androidx.room.Entity

@Entity(primaryKeys = ["run_id", "user_id"])
data class RunUserCrossRef(
    val run_id: Int,
    val user_id: Int
)