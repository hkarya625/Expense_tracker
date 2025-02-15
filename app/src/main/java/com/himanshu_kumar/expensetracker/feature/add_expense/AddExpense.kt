package com.himanshu_kumar.expensetracker.feature.add_expense

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.himanshu_kumar.expensetracker.R
import com.himanshu_kumar.expensetracker.Utils
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import com.himanshu_kumar.expensetracker.ui.theme.fontFamily
import com.himanshu_kumar.expensetracker.viewmodel.AddExpenseViewModel
import com.himanshu_kumar.expensetracker.viewmodel.AddExpenseViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddExpense(navController: NavController){

    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_toolbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Text(
                    text = "Add Expense",
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            DataForm(modifier = Modifier
                .padding(top = 60.dp)
                .constrainAs(card){
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                },
                onAddExpenseClick = {
                    coroutineScope.launch {

                       if(viewModel.addExpense(it)){
                           navController.popBackStack()
                       }
                    }
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier: Modifier, onAddExpenseClick:(model:ExpenseEntity) ->Unit)
{
    val name = remember{mutableStateOf("")}
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableLongStateOf(0L) }
    val dateVisibility = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }

    val lend_to_visibility = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(text = "Name", fontFamily = fontFamily, fontSize = 14.sp)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.size(8.dp))

        Text(text = "Amount", fontFamily = fontFamily, fontSize = 14.sp)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = {
                amount.value = it
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.size(8.dp))

        Text(text = "Date", fontFamily = fontFamily, fontSize = 14.sp)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = if(date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(date.longValue),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    dateVisibility.value = true
                },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.DarkGray,
                disabledTextColor = Color.Black
            )
        )
        Spacer(Modifier.size(8.dp))

        Text(text = "Category", fontFamily = fontFamily, fontSize = 14.sp)
        Spacer(Modifier.size(4.dp))
        ExpenseDropDown(
            list = listOf(
                "Expense",
                "Payment",
                "Shopping",
                "Food",
                "Salary",
                "Recharge",
                "Lend"
            ),
            onItemSelected = {
                category.value = it
            }
        )
        Spacer(Modifier.size(8.dp))

        if(category.value == "Lend"){
            Text(text = "Lend To", fontFamily = fontFamily, fontSize = 14.sp)
            Spacer(Modifier.size(4.dp))
            OutlinedTextField(
                value = amount.value,
                onValueChange = {
                    amount.value = it
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.size(8.dp))
        }
        Text(text = "Type", fontFamily = fontFamily, fontSize = 14.sp)
        Spacer(Modifier.size(4.dp))
        ExpenseDropDown(
            list = listOf(
                "Income",
                "Expense",
                "Lend"
            ),
            onItemSelected = {
                type.value = it
            }
        )
        Spacer(Modifier.size(20.dp))


        Button(
            onClick = {
                val model = ExpenseEntity(
                    id = null,
                    title = name.value,
                    amount = amount.value.toDoubleOrNull()?:0.0,
                    date = date.longValue,
                    category = category.value,
                    type = type.value
                )
                Log.d("Button_clicked_TAG", "DataForm: $model")
                onAddExpenseClick(model)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.card_color)
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            Text(
                text = "Add Expense",
                fontFamily = fontFamily,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
    if(dateVisibility.value){
        ExpenseDatePicker(
            onDateSelected = {
                date.value = it
                dateVisibility.value = false
            }, onDismiss = {
                dateVisibility.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePicker(
    onDateSelected: (date:Long) -> Unit,
    onDismiss:()->Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?:0L
    DatePickerDialog(
        onDismissRequest = {onDismiss()},
        confirmButton = {
            TextButton(onClick = {onDateSelected(selectedDate)}) { Text(text = "Confirm") }
        },
        dismissButton = {
            TextButton(onClick = {onDateSelected(selectedDate)}) { Text(text = "Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(list:List<String>, onItemSelected:(item:String)->Unit){

    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<String>(list[0]) }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value  = it}) {
        TextField(value = selectedItem.value, onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(5.dp),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.White,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            list.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        selectedItem.value = it
                        onItemSelected(selectedItem.value)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddExpensePreview()
{
    AddExpense(rememberNavController())
}