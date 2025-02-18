package com.himanshu_kumar.expensetracker.feature.stats

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
        } else {
            viewModel.incomeEntries.collectAsState(emptyList()) to viewModel.topIncome.collectAsState(emptyList())
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
            TransactionList(
                modifier = Modifier,
                list = transactionList.value,
                title = "Top Spending"
            )
        }
    }
}


@Composable
fun TransactionTypeDropDown(
    onTypeChange:(String)->Unit,
    modifier: Modifier = Modifier
){

    var dropDownExpand by remember { mutableStateOf(true) }
    val transactionType = listOf("Expense", "Income")


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

@Preview(showBackground = true)
@Composable
fun PreviewStatsScreen()
{
    val navController = rememberNavController()
    StatsScreen(navController)
}