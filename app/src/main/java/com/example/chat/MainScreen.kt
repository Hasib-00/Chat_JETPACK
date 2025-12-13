package com.example.chat

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.sent.home.HomeScreen

data class NavItem(
    val title: String,
    val selectedicon: ImageVector,
    val unselectedicon: ImageVector
)

val navItem = listOf(
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("Chat", Icons.Filled.Chat, Icons.Outlined.Chat),
    NavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun MainScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItem.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(
                                imageVector =
                                    if (index == selectedIndex)
                                        item.selectedicon
                                    else
                                        item.unselectedicon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedIndex) {
            0 -> HomeScreen(
                modifier = Modifier.padding(paddingValues),
                onChat = { userId ->
                    navController.navigate("chat/$userId")
                }
            )
            // 1 -> ChatScreen(modifier = Modifier.padding(paddingValues))
            // 2 -> ProfileScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}
