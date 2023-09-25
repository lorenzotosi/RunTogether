package com.app.runtogether.db.user

import android.content.Context
import androidx.room.*
import com.app.runtogether.db.Database

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :first LIMIT 1")
    fun findByUsername(first: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    companion object{
        private var INSTANCE: Database? = null

        private fun buildDatabase(context: Context) : Database {
            return Room.databaseBuilder(context, Database::class.java, "user-db").build()
        }

        fun getDatabaseInstance(context: Context) : Database {
            if(INSTANCE == null){
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }
    }
}