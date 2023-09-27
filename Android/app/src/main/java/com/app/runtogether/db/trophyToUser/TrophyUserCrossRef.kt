package com.app.runtogether.db.trophyToUser

import androidx.room.Entity

@Entity(primaryKeys = ["user_id", "trophy_id"])
data class TrophyUserCrossRef(
    val user_id: Int,
    val trophy_id: Int
)