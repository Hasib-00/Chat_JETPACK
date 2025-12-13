package com.example.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat.Profile.ProfileScreen
import com.example.chat.auth.LoginScreen
import com.example.chat.auth.RegisterScreen
import com.example.chat.ui.theme.ChatTheme
import com.example.sent.home.HomeScreen

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
                    composable("Login") {
                        LoginScreen(navController)
                    }

                    composable("Register") {
                        RegisterScreen(navController)
                    }

                    composable("Home") {
                        HomeScreen { userId ->
                            navController.navigate("chat/$userId")
                        }
                    }
                    composable("Main") {
                        MainScreen(navController)
                    }
                    composable("Profile") {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}
