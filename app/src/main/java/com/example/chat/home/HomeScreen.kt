package com.example.chat.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.model.UserModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = viewModel(),
    onChatClick: (UserModel) -> Unit
) {
    val state by vm.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = "Chats",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        when {
            state.loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.users.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No chats yet")
                }
            }

            else -> {
                LazyColumn {
                    items(state.users) { user ->
                        MessengerChatCard(user) {
                            onChatClick(user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessengerChatCard(
    user: UserModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color(0xFF1877F2)),
            contentAlignment = Alignment.Center
        ) {
            val avatarLetter = (
                    user.username.firstOrNull { it.isLetterOrDigit() }
                        ?: user.email.firstOrNull()
                    )?.uppercase() ?: "?"

            Text(
                text = avatarLetter.toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.username.ifEmpty { user.email },
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Tap to start chatting",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Text(
            text = "Now",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
