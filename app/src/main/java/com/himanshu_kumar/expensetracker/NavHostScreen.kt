package com.himanshu_kumar.expensetracker

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.himanshu_kumar.expensetracker.feature.add_expense.AddExpense
import com.himanshu_kumar.expensetracker.feature.home.HomeScreen
import com.himanshu_kumar.expensetracker.feature.stats.StatsScreen
import com.himanshu_kumar.expensetracker.feature.welcome.NameInputScreen
import com.himanshu_kumar.expensetracker.feature.welcome.WelcomeScreen
import com.himanshu_kumar.expensetracker.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun NavHostScreen(
    userViewModel: UserViewModel
) {
    var userName by remember { mutableStateOf("") }
    var savedName by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var bottomBarVisibility by remember { mutableStateOf(true) }
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        savedName = userViewModel.loadUserName()
        isLoading = false
    }

    if (isLoading) return

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
            composable("/home") {
                bottomBarVisibility = true
                HomeScreen(
                    navController = navController,
                    userName = savedName?.takeIf { it.isNotBlank() } ?: userName
                )
            }
            composable("/add") {
                bottomBarVisibility = false
                AddExpense(navController)
            }
            composable("/stats") {
                bottomBarVisibility = true
                StatsScreen(navController)
            }
            composable("/welcome") {
                bottomBarVisibility = false
                WelcomeScreen(navController)
            }
            composable("/name") {
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
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
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
                    selectedIconColor = colorResource(R.color.card_color),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
