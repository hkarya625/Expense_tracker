package com.himanshu_kumar.expensetracker

import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {


    fun formatDateToHumanReadableForm(dateInMillis:Long):String{
        val dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return dateFormat.format(dateInMillis)
    }

    fun formatDateForChart(dateInMillis:Long):String{
        val dateFormat = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormat.format(dateInMillis)
    }

    fun formatToDecimalValue(d:Double):String{
        return String.format("%.2f", d)
    }


    fun getMillisFromDate(date:String):Long{
        return getMilliFromDate(date)
    }
    fun getMilliFromDate(dateFormat:String?):Long{
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        }catch (e: ParseException)
        {
            e.printStackTrace()
        }
        return date.time
    }


    fun getItemIcon(item: ExpenseEntity):Int{
        if(item.category == "Payment")
            return R.drawable.ic_upi

        if(item.category == "Shopping")
            return R.drawable.ic_shopping

        if(item.category == "Food")
            return R.drawable.ic_food

        if(item.category == "Salary")
            return R.drawable.ic_salary

        if(item.category == "Recharge")
            return R.drawable.ic_recharge

        if(item.category == "Lend")
            return R.drawable.ic_lend

        return R.drawable.ic_rs
    }
}