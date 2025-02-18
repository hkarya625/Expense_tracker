package com.himanshu_kumar.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.expensetracker.data.ExpenseDataBase
import com.himanshu_kumar.expensetracker.data.UserPreferences


import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserPreferences): ViewModel()  {


     fun saveUserName(name:String){
        viewModelScope.launch {
            userRepository.saveUserName(name)
        }
    }

    suspend fun loadUserName():String? {
       return userRepository.userName.first()
    }
}

class UserViewModelFactory(private val userPreferences: UserPreferences): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(
                UserViewModel::class.java
            )){
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}