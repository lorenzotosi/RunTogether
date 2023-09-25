package com.app.runtogether.db.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val users = repository.users

    fun addNewUser(user : User) = viewModelScope.launch{
        repository.insertNewUser(user)
    }





}