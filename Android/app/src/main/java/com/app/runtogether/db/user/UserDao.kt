package com.app.runtogether.db.user

import android.content.Context
import androidx.room.*
import com.app.runtogether.db.MyDatabase

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
        private var INSTANCE: MyDatabase? = null

        private fun buildDatabase(context: Context) : MyDatabase {
            return Room.databaseBuilder(context, MyDatabase::class.java, "user-db").build()
        }

        fun getDatabaseInstance(context: Context) : MyDatabase {
            if(INSTANCE == null){
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }
    }
}