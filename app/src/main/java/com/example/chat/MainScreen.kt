package com.example.chat.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.chat.Profile.ProfileScreen
import com.example.chat.home.HomeScreen

@Composable
fun MainScreen(navController: NavController) {

    val tabs = listOf("Home", "Chat", "Profile")
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { i, t ->
                    NavigationBarItem(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        icon = {
                            Icon(
                                imageVector = when (t) {
                                    "Home" -> Icons.Filled.Home
                                    "Chat" -> Icons.Filled.Chat
                                    else -> Icons.Filled.Person
                                },
                                contentDescription = null
                            )
                        },
                        label = { Text(t) }
                    )
                }
            }
        }
    ) { padding ->

        when (selectedTab) {
            0 -> HomeScreen(
                modifier = Modifier.padding(padding),
                onChatClick = { user ->
                    navController.navigate("chat/${user.id}")
                }
            )
            // 1 -> could be recent chats tab later
            2 -> ProfileScreen(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
