package com.app.runtogether.db.trophyToUser

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["user_id", "trophy_id"], indices = [Index(value = ["trophy_id"])])
data class TrophyUserCrossRef(
    val user_id: Int,
    val trophy_id: Int
)