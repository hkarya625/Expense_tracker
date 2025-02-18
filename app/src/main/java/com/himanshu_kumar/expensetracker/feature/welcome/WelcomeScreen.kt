package com.himanshu_kumar.expensetracker.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himanshu_kumar.expensetracker.R
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    navController: NavController
){
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        HorizontalPager(
            state = pagerState
        ) {
                page->
            when(page){
                0->{
                    OnBoardingPage(
                        image = R.drawable.intro_welcome,
                        title = stringResource(R.string.intro_image1_title),
                        description = stringResource(R.string.intro_image1_des)
                    )
                }
                1->{
                    OnBoardingPage(
                        image = R.drawable.intro_image,
                        title = stringResource(R.string.intro_image2_title),
                        description = stringResource(R.string.intro_image2_des)
                    )
                }

            }
        }

        DotIndicator(
            totalDots = 2,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = {
                if(pagerState.currentPage == 1){
                    navController.navigate("/name"){
                        popUpTo("/welcome"){
                            inclusive = true
                            saveState = true
                        }
                    }
                }else{
                    coroutineScope.launch {
                        // pagerState.scrollToPage(0)
                        pagerState.animateScrollToPage(1)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.card_color)
            )
        ) {
            Text(text = if(pagerState.currentPage == 1) "Get Started" else "Next",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}



@Composable
fun OnBoardingPage(
    image:Int,
    title:String,
    description:String
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = colorResource(R.color.dark_color1),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            color = colorResource(R.color.dark_color2)
        )
    }
}




@Composable
fun DotIndicator(
    totalDots:Int,
    selectedIndex:Int,
    modifier:Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots){
                index->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if(index == 0) 12.dp else 8.dp)
                    .then(
                        modifier.background(
                            color = if(index == selectedIndex) colorResource(R.color.card_color) else colorResource(R.color.dark_color4),
                            shape = MaterialTheme.shapes.small
                        )
                    ),
            ) {

            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun WelcomeScreenPreview()
//{
//    WelcomeScreen()
//}