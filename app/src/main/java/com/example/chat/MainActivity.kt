package com.example.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chat.auth.LoginScreen
import com.example.chat.auth.RegisterScreen
import com.example.chat.chat.ChatScreen
import com.example.chat.screens.MainScreen
import com.example.chat.ui.theme.ChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChatTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "Login"
                ) {
                    composable("Login") { LoginScreen(navController) }
                    composable("Register") { RegisterScreen(navController) }
                    composable("Main") { MainScreen(navController) }

                    // Chat screen route only needs userId now
                    composable(
                        "chat/{userId}",
                        arguments = listOf(
                            navArgument("userId") { type = NavType.StringType }
                        )
                    ) { entry ->
                        val userId = entry.arguments?.getString("userId") ?: ""
                        ChatScreen(
                            userId = userId,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
