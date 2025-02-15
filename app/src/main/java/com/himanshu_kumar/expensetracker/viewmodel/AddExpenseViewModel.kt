package com.himanshu_kumar.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.himanshu_kumar.expensetracker.data.ExpenseDataBase
import com.himanshu_kumar.expensetracker.data.dao.ExpenseDao
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity

class AddExpenseViewModel(private val dao:ExpenseDao):ViewModel() {
    suspend fun addExpense(expenseEntity: ExpenseEntity):Boolean{
        return try {
            dao.insertExpense(expenseEntity)
            true
        }catch (ex:Throwable){
            false
        }
    }
}

class AddExpenseViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(
                AddExpenseViewModel::class.java
            )){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}