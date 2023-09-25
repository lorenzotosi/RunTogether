package com.app.runtogether.db.user

import android.content.Context
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    companion object{
        private var INSTANCE: UserDatabase? = null

        private fun buildDatabase(context: Context) : UserDatabase {
            return Room.databaseBuilder(context, UserDatabase::class.java, "user-db").build()
        }

        fun getDatabaseInstance(context: Context) : UserDatabase {
            if(INSTANCE == null){
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }
    }
}