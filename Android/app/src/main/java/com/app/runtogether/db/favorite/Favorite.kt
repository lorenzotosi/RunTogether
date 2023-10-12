package com.app.runtogether.db.favorite

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["user_id", "trophy_id"], indices = [Index(value = ["trophy_id"])])
data class Favorite(
    val user_id: Int,
    val trophy_id: Int
)