package com.himanshu_kumar.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.himanshu_kumar.expensetracker.Utils
import com.himanshu_kumar.expensetracker.data.ExpenseDataBase
import com.himanshu_kumar.expensetracker.data.dao.ExpenseDao
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import com.himanshu_kumar.expensetracker.data.model.ExpenseSummary


class StatsViewModel(private val dao: ExpenseDao): ViewModel() {
    val expenseEntries = dao.getAllExpenseByDate()
    val topExpense = dao.getTopExpenses()

    val incomeEntries = dao.getAllIncomeByDate()
    val topIncome = dao.getTopIncome()

    val lendEntries = dao.getAllLendByDate()
    val topLend = dao.getTopLend()

    fun getEntriesForChart(entries:List<ExpenseSummary>):List<Entry>{              // used to change the data from ExpenseSummary to Entry
        val list = mutableListOf<Entry>()
        for(entry in entries)
        {
            list.add(Entry(entry.date.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }

    fun getIncomeEntriesForChart(incomeEntries:List<ExpenseSummary>):List<Entry>{
        val list = mutableListOf<Entry>()
        for (entry in incomeEntries){
            list.add(Entry(entry.date.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(
                StatsViewModel::class.java
            )){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}