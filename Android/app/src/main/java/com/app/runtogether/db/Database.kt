package com.app.runtogether.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.runtogether.db.trophy.Trophy
import com.app.runtogether.db.trophy.TrophyDao
import com.app.runtogether.db.user.User
import com.app.runtogether.db.user.UserDao

@Database(entities = [User::class, Trophy::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun trophyDao(): TrophyDao
}