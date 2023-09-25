package com.app.runtogether.db.user

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val users: Flow<List<User>> = userDao.getAll()

    @WorkerThread
    suspend fun insertNewUser(user : User){
        userDao.insertAll(user)
    }

}