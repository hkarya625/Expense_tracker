package com.himanshu_kumar.expensetracker.data.model

data class ExpenseSummary(
    val type:String,
    val date:String,
    val total_amount:Double,
    val lendedTo:String? = null
)
