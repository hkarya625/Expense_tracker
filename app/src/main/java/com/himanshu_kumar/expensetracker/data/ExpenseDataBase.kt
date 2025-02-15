package com.himanshu_kumar.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.himanshu_kumar.expensetracker.data.dao.ExpenseDao
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity


@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase:RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object{
        const val DATABASE_NAME = "expense_db"

        @JvmStatic
        fun getDatabase(context: Context):ExpenseDataBase{
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}