package com.example.budgetproapplication.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetproapplication.ui.screens.DashboardScreen
import com.example.budgetproapplication.ui.screens.HomeScreen
import com.example.budgetproapplication.ui.screens.LoginScreen
import com.example.budgetproapplication.ui.screens.SignupScreen
import com.example.budgetproapplication.ui.screens.AddExpenseScreen
import com.example.budgetproapplication.ui.screens.ViewExpensesScreen
import com.example.budgetproapplication.ui.screens.SetGoalsScreen
import com.example.budgetproapplication.ui.screens.ViewTotalsScreen
import com.example.budgetproapplication.ui.viewmodel.AuthState
import com.example.budgetproapplication.ui.viewmodel.AuthViewModel
import com.example.budgetproapplication.ui.viewmodel.ExpenseViewModel
import com.example.budgetproapplication.ui.viewmodel.GoalViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object Signup : AuthScreen("signup")
    object Dashboard : AuthScreen("dashboard")
    object AddExpense : AuthScreen("add_expense")
    object ViewExpenses : AuthScreen("view_expenses")
    object SetGoals : AuthScreen("set_goals")
    object ViewTotals : AuthScreen("view_totals")
}

@Composable
fun AuthNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    expenseViewModel: ExpenseViewModel,
    goalViewModel: GoalViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(AuthScreen.Dashboard.route) {
                    popUpTo(AuthScreen.Login.route) { inclusive = true }
                }
            }
            is AuthState.Initial -> {
                navController.navigate(AuthScreen.Login.route) {
                    popUpTo(AuthScreen.Dashboard.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthScreen.Login.route
    ) {
        composable(AuthScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AuthScreen.Dashboard.route) {
                        popUpTo(AuthScreen.Login.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(AuthScreen.Signup.route) {
            SignupScreen(
                onSignupClick = { name, email, password ->
                    authViewModel.signUp(name, email, password)
                },
                onLoginClick = {
                    navController.navigateUp()
                },
                viewModel = authViewModel
            )
        }

        composable(AuthScreen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAddExpense = {
                    navController.navigate(AuthScreen.AddExpense.route)
                },
                onNavigateToViewExpenses = {
                    navController.navigate(AuthScreen.ViewExpenses.route)
                },
                onNavigateToSetGoals = {
                    navController.navigate(AuthScreen.SetGoals.route)
                },
                onNavigateToViewTotals = {
                    navController.navigate(AuthScreen.ViewTotals.route)
                }
            )
        }

        composable(AuthScreen.AddExpense.route) {
            AddExpenseScreen(
                onNavigateBack = { navController.navigateUp() },
                expenseViewModel = expenseViewModel
            )
        }

        composable(AuthScreen.ViewExpenses.route) {
            ViewExpensesScreen(
                onNavigateBack = { navController.navigateUp() },
                expenseViewModel = expenseViewModel
            )
        }

        composable(AuthScreen.SetGoals.route) {
            SetGoalsScreen(
                onNavigateBack = { navController.navigateUp() },
                goalViewModel = goalViewModel
            )
        }

        composable(AuthScreen.ViewTotals.route) {
            ViewTotalsScreen(
                navController = navController,
                expenseViewModel = expenseViewModel
            )
        }
    }
} 