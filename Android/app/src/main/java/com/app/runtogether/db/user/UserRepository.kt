package com.app.runtogether.db.user

class UserRepository(private val UserDatabase: UserDatabase){
    fun insertUser(user: User) = UserDatabase.userDao().insertAll(user)
    fun deleteUser(user: User) = UserDatabase.userDao().delete(user)
    fun getAllUsers() = UserDatabase.userDao().getAll()
    fun loadAllByIds(userId: IntArray) = UserDatabase.userDao().loadAllByIds(userId)
    fun findByName(first: String, last: String) = UserDatabase.userDao().findByName(first, last)
}