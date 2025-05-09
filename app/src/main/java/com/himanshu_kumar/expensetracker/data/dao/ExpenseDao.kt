package com.himanshu_kumar.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import com.himanshu_kumar.expensetracker.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT *FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT *FROM expense_table WHERE type = 'Expense' ORDER BY amount DESC LIMIT 5")
    fun getTopExpenses(): Flow<List<ExpenseEntity>>


    @Query("SELECT *FROM expense_table WHERE type = 'Income' ORDER BY amount DESC LIMIT 5")
    fun getTopIncome():Flow<List<ExpenseEntity>>

    @Query("SELECT type, date, SUM(amount) AS total_amount FROM expense_table where type = 'Income' GROUP BY type, date ORDER BY date")
    fun getAllIncomeByDate(): Flow<List<ExpenseSummary>>

    @Query("SELECT type, date, SUM(amount) AS total_amount FROM expense_table where type = :type GROUP BY type, date ORDER BY date")
    fun getAllExpenseByDate(type:String = "Expense"): Flow<List<ExpenseSummary>>



    @Query("SELECT type, date, SUM(amount) AS total_amount, lendedTo FROM expense_table where type = 'Lend' GROUP BY type, date ORDER BY date")
    fun getAllLendByDate(): Flow<List<ExpenseSummary>>

    @Query("SELECT *FROM expense_table WHERE type = 'Lend' ORDER BY amount DESC LIMIT 5")
    fun getTopLend():Flow<List<ExpenseEntity>>


    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntity)
}