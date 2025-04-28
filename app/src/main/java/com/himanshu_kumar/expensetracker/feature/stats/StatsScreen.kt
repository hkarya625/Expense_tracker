package com.himanshu_kumar.expensetracker.feature.stats

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.himanshu_kumar.expensetracker.R
import com.himanshu_kumar.expensetracker.Utils
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import com.himanshu_kumar.expensetracker.feature.home.TransactionItem
import com.himanshu_kumar.expensetracker.feature.home.TransactionList
import com.himanshu_kumar.expensetracker.ui.theme.fontFamily
import com.himanshu_kumar.expensetracker.viewmodel.StatsViewModel
import com.himanshu_kumar.expensetracker.viewmodel.StatsViewModelFactory

@Composable
fun StatsScreen(navController: NavController){

    var transactionType by remember { mutableStateOf("Expense") }
    var transactionDropDown by remember{ mutableStateOf(false) }


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            navController.popBackStack()
                        },
                    colorFilter = ColorFilter.tint(Color.Black)
                )
                Text(
                    text = "Statistic",
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.card_color),
                    modifier = Modifier.padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            transactionDropDown = !transactionDropDown
                        },
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    ) {
        
        val viewModel = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)

        val (dataState, transactionList) = if (transactionType == "Expense") {
            viewModel.expenseEntries.collectAsState(emptyList()) to viewModel.topExpense.collectAsState(emptyList())
        } else if(transactionType == "Income") {
            viewModel.incomeEntries.collectAsState(emptyList()) to viewModel.topIncome.collectAsState(emptyList())
        }else{
            viewModel.lendEntries.collectAsState(emptyList()) to viewModel.topLend.collectAsState(emptyList())
        }

        Column(
            modifier = Modifier.padding(it)
        ) {

            if(transactionDropDown){
                TransactionTypeDropDown(
                    onTypeChange = {
                        transactionDropDown = false
                        transactionType = it
                    }
                )
            }

            val entries = viewModel.getEntriesForChart(dataState.value)
            LineChart(entries)
            Spacer(Modifier.height(16.dp))

            if(transactionType == "Lend"){
                LendedList(
                    modifier = Modifier,
                    list = transactionList.value,
                    title = "Recent Transactions"
                )
            }else{
                TransactionList(
                    modifier = Modifier,
                    list = transactionList.value,
                    title = if(transactionType == "Expense") "Top Expenses" else "Top Income"
                )
            }
        }
    }
}


@Composable
fun TransactionTypeDropDown(
    onTypeChange:(String)->Unit,
    modifier: Modifier = Modifier
){

    var dropDownExpand by remember { mutableStateOf(true) }
    val transactionType = listOf("Expense", "Income", "Lend")


    Box(
        modifier = modifier.fillMaxWidth().padding(start = 350.dp),  // Takes full width
        contentAlignment = Alignment.TopEnd // Align content to the right
    ){
        DropdownMenu(
            expanded = dropDownExpand,
            onDismissRequest = {
                dropDownExpand = false
            }
        ) {
            transactionType.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = type,
                            fontFamily = fontFamily,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        onTypeChange(type)
                    }
                )
            }
        }
    }

}

@Composable
fun LineChart(entries:List<Entry>){
    val context = LocalContext.current
    AndroidView(
        factory = {
            val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
            view
        }, modifier = Modifier.fillMaxWidth().height(250.dp)
    ){  view->
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)
        val dataSet = LineDataSet(entries, "Expense").apply {
            color = android.graphics.Color.parseColor("#FF2F7E79")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
            val drawable = ContextCompat.getDrawable(context, R.drawable.chart_gradient)
            drawable?.let {
                fillDrawable = it
            }
        }

        lineChart.xAxis.valueFormatter =
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Utils.formatDateForChart(value.toLong())
                }
            }
        lineChart.data = LineData(dataSet)
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.invalidate()

    }
}




@Composable
fun LendedList(modifier: Modifier, list:List<ExpenseEntity>, title: String){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
            ){
                Text(text = title,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp)
                if(title == "Recent Transactions")
                {
                    Text(
                        text = "See All",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        items(list){
                item ->
            LendListItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = Utils.getItemIcon(item),
                date = Utils.formatDateToHumanReadableForm(dateInMillis = item.date),
                color = if(item.type == "Income") Color.Green else Color.Red
            )
        }

    }
}


@Composable
fun LendListItem(title: String, amount: String, icon: Int, date:String, color:Color){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = fontFamily,
                    fontSize = 16.sp
                )
                Text(
                    text = date,
                    fontFamily = fontFamily,
                    fontSize = 12.sp
                )
            }
        }
        Text(
            text = "₹ $amount",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )

//        Checkbox(
//            checked = isPaid,
//            onCheckedChange = {
//                onChecked(true)
//            },
//            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 20.dp)
//        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStatsScreen()
{
    val navController = rememberNavController()
    StatsScreen(navController)
}