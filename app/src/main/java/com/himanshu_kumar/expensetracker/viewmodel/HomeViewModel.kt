package com.himanshu_kumar.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.himanshu_kumar.expensetracker.R
import com.himanshu_kumar.expensetracker.Utils
import com.himanshu_kumar.expensetracker.data.ExpenseDataBase
import com.himanshu_kumar.expensetracker.data.dao.ExpenseDao
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity

class HomeViewModel(dao: ExpenseDao):ViewModel() {
    val expense = dao.getAllExpenses()

    fun getBalance(expense:List<ExpenseEntity>):String{
        var total = 0.0
        expense.forEach{
            if(it.type == "Income"){
                total+=it.amount
            }
            else{
                total-=it.amount
            }
        }
        return "₹ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalExpense(expense:List<ExpenseEntity>):String{
        var total = 0.0
        expense.forEach{
            if(it.type == "Expense"){
                total+=it.amount
            }
        }
        return "₹ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalIncome(expense:List<ExpenseEntity>):String{
        var total = 0.0
        expense.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "₹ ${Utils.formatToDecimalValue(total)}"
    }


}

class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(
            HomeViewModel::class.java
        )){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}