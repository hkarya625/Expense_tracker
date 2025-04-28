package com.himanshu_kumar.expensetracker.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.himanshu_kumar.expensetracker.R
import com.himanshu_kumar.expensetracker.Utils
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import com.himanshu_kumar.expensetracker.ui.theme.fontFamily
import com.himanshu_kumar.expensetracker.viewmodel.HomeViewModel
import com.himanshu_kumar.expensetracker.viewmodel.HomeViewModelFactory
import com.himanshu_kumar.expensetracker.viewmodel.UserViewModel


@Composable
fun HomeScreen(navController: NavController, userName:String){
    val viewModel:HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (nameRow, list, card, topBar, add) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_toolbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                Column {
                    Text(text = "Good Afternoon, ", fontWeight = FontWeight.SemiBold, fontFamily = fontFamily, fontSize = 16.sp, color = Color.White)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = userName,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            val state = viewModel.expense.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            CardItem(
                modifier = Modifier
                    .constrainAs(card){
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                balance, income, expense
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(add.top)
                        height = Dimension.fillToConstraints
                    },
                list = state.value
            )
            Image(
                painter = painterResource(R.drawable.ic_addbutton),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(add){
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(bottom = 10.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("/add")
                    }
            )
        }
    }
}

@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expense: String){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.card_color))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(text = "Total Balance",fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 20.sp, color = Color.White)
                Text(
                    text = balance,
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                icon = R.drawable.ic_income
            )
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                icon = R.drawable.ic_expense
            )
        }


    }
}

@Composable
fun TransactionList(modifier: Modifier, list:List<ExpenseEntity>, title: String = "Recent Transactions"){
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
            TransactionItem(
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
fun CardRowItem(modifier: Modifier, title: String, amount:String, icon: Int){
    Column(
        modifier = modifier
    ) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null
            )
//            Spacer(Modifier.size(8.dp))
            Text(text = title,
                fontFamily = fontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically))
        }
        Spacer(Modifier.size(2.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = amount,
            fontFamily = fontFamily,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun TransactionItem(title: String, amount: String, icon: Int, date:String, color:Color){
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
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen()
{
    HomeScreen(
        rememberNavController(),
        userName = "Himanshu Arya"
    )
}