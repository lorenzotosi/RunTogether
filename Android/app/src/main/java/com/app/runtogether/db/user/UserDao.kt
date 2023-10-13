package com.app.runtogether.db.user

import android.content.Context
import androidx.room.*
import com.app.runtogether.db.MyDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE username LIKE :first LIMIT 1")
    suspend fun findByUsername(first: String): User


    @Query("SELECT username FROM user WHERE user_id = :id")
    fun getUsernameFromId(id: Int): Flow<String>

    @Query("UPDATE user SET path = :path WHERE user_id = :id")
    fun addPathToUser(path: String, id: Int)

    @Query("SELECT path FROM user WHERE user_id = :id")
    fun getPathFromId(id: Int): Flow<String>

    @Insert
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}