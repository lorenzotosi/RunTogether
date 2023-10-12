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

    @Query("UPDATE user SET image = :image WHERE user_id = :id")
    fun addImgeToUser(image: ByteArray, id: Int)

    @Query("SELECT image FROM user WHERE user_id = :id")
    fun getImgeFromId(id: Int): Flow<ByteArray>

    @Insert
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}