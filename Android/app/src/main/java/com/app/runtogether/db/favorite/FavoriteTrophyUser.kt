package com.app.runtogether.db.favorite

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.app.runtogether.db.trophy.Trophy
import com.app.runtogether.db.user.User

data class FavoriteTrophyUser(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "trophy_id",
        associateBy = Junction(Favorite::class)
    )
    val trophy: List<Trophy>
)