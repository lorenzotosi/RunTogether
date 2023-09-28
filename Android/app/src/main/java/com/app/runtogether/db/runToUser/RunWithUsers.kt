package com.app.runtogether.db.runToUser

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.app.runtogether.db.run.Run
import com.app.runtogether.db.user.User

data class RunWithUsers(
    @Embedded val user: Run,
    @Relation(
        parentColumn = "run_id",
        entityColumn = "user_id",
        associateBy = Junction(RunUserCrossRef::class)
    )
    val users: List<User>
)