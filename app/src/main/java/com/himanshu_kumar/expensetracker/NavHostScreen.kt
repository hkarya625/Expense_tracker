package com.himanshu_kumar.expensetracker

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.himanshu_kumar.expensetracker.data.UserPreferences
import com.himanshu_kumar.expensetracker.feature.add_expense.AddExpense
import com.himanshu_kumar.expensetracker.feature.home.HomeScreen
import com.himanshu_kumar.expensetracker.feature.stats.StatsScreen
import com.himanshu_kumar.expensetracker.feature.welcome.NameInputScreen
import com.himanshu_kumar.expensetracker.feature.welcome.WelcomeScreen
import com.himanshu_kumar.expensetracker.viewmodel.UserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavHostScreen(
    userViewModel: UserViewModel
) {
    var userName by remember { mutableStateOf("") }
    var savedName by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) } // Track loading state

    val navController = rememberNavController()
    var bottomBarVisibility by remember { mutableStateOf(true) }

    // Fetch stored username before setting start destination
    LaunchedEffect(Unit) {
        savedName = userViewModel.loadUserName()
        isLoading = false // Mark loading as complete
    }

    if (isLoading) return // Wait until loading is complete

    val startDestination = if (savedName.isNullOrBlank()) "/welcome" else "/home"

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisibility) {
                NavigationBottomBar(
                    navController = navController,
                    items = listOf(
                        NavItem(route = "/home", icon = R.drawable.ic_home),
                        NavItem(route = "/stats", icon = R.drawable.ic_stats)
                    )
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(animationSpec = tween(2000)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(2000)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(2000)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(2000)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(2000)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(2000)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(2000)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(2000)
                )
            }
        ) {
            composable("/home",

            ) {
                bottomBarVisibility = true
                HomeScreen(
                    navController = navController,
                    userName = if(savedName.isNullOrBlank()) userName else savedName.toString()
                )
            }
            composable("/add",

                ) {
                bottomBarVisibility = false
                AddExpense(navController)
            }
            composable("/stats",

                ) {
                bottomBarVisibility = true
                StatsScreen(navController)
            }
            composable("/welcome",

                ) {
                bottomBarVisibility = false
                WelcomeScreen(navController)
            }
            composable("/name",

                ) {
                bottomBarVisibility = false
                NameInputScreen { name ->
                    userViewModel.viewModelScope.launch {
                        userViewModel.saveUserName(name)
                        userName = name
                    }
                    navController.navigate("/home") {
                        popUpTo("/name") {
                            inclusive = true
                            saveState = true
                        }
                    }
                }
            }
        }
    }
}

data class NavItem(
    val route:String,
    val icon:Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items:List<NavItem>
){
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach{
                item->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = colorResource(R.color.card_color),
                    selectedIconColor = colorResource(R.color.card_color),
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}