package com.himanshu_kumar.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.himanshu_kumar.expensetracker.data.UserPreferences
import com.himanshu_kumar.expensetracker.feature.welcome.SplashScreen
import com.himanshu_kumar.expensetracker.ui.theme.ExpenseTrackerTheme
import com.himanshu_kumar.expensetracker.viewmodel.UserViewModel
import com.himanshu_kumar.expensetracker.viewmodel.UserViewModelFactory
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            val userPreferences = remember { UserPreferences(this) }
            val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userPreferences))

            // Launch a coroutine to hide the splash screen after a delay
            LaunchedEffect(Unit) {
                delay(2000) // Show splash screen for 2 seconds
                showSplash = false
            }

            if (showSplash) {
                SplashScreen()
            } else {
               NavHostScreen(userViewModel)
            }
        }
    }
}
