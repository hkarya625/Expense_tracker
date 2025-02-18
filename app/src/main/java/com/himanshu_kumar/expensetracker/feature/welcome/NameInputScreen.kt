package com.himanshu_kumar.expensetracker.feature.welcome

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshu_kumar.expensetracker.R
import com.himanshu_kumar.expensetracker.data.model.ExpenseEntity
import com.himanshu_kumar.expensetracker.ui.theme.fontFamily

@Composable
fun NameInputScreen(
    onNameEntered:(name:String)->Unit
){
    val name = remember{mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = "app_logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.size(25.dp))
        Text(
            text = stringResource(R.string.ask_name),
            fontFamily = fontFamily,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            singleLine = true
        )
        Spacer(Modifier.size(10.dp))
        Button(
            onClick = {
                onNameEntered(name.value)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.card_color)
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
        ) {
            Text(
                text = "Next",
                fontFamily = fontFamily,
                fontSize = 15.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.size(125.dp))
    }

}

@Preview(showBackground = true)
@Composable
fun NameInputScreenPreview(){
    NameInputScreen(onNameEntered = {})
}