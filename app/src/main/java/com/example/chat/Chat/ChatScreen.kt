package com.example.chat.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: String,
    navController: NavController,
    vm: ChatViewModel = viewModel()
) {
    // Load opponent user + messages when we enter this screen
    LaunchedEffect(userId) {
        vm.loadChatUserInfo(userId)
        vm.loadMessages(userId)
    }

    val opponent by vm.chatUser.collectAsState()
    val messages by vm.messages.collectAsState()
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val myEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Me"

    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ChatHeader(
                user = opponent,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    val avatarName =
                        if (msg.senderId == currentUid) myEmail
                        else opponent?.username ?: opponent?.email ?: "User"

                    MessageBubble(
                        text = msg.message,
                        timestamp = msg.timestamp,
                        isMe = msg.senderId == currentUid,
                        avatarName = avatarName
                    )
                }
            }

            // Scroll to bottom when messages change
            LaunchedEffect(messages) {
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(0)
                }
            }

            // Input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFEFEFEF), CircleShape)
                        .padding(12.dp)
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            vm.sendMessage(userId, text)
                            text = ""
                            focusManager.clearFocus()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color(0xFF7C4DFF)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatHeader(
    user: UserModel?,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7C4DFF))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        AvatarBubble(user)

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = user?.username ?: user?.email ?: "User",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Active now",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun MessageBubble(
    text: String,
    timestamp: Long,
    isMe: Boolean,
    avatarName: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.Bottom) {

            if (!isMe) {
                SmallAvatarBubble(avatarName)
                Spacer(modifier = Modifier.width(6.dp))
            }

            Box(
                modifier = Modifier
                    .background(
                        color = if (isMe) Color(0xFF7C4DFF) else Color(0xFFEDE7FF),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(12.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text = text,
                    color = if (isMe) Color.White else Color.Black
                )
            }

            if (isMe) {
                Spacer(modifier = Modifier.width(6.dp))
                SmallAvatarBubble(avatarName)
            }
        }

        Text(
            text = formatTimestamp(timestamp),
            color = Color.Gray,
            fontSize = 11.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun AvatarBubble(user: UserModel?) {
    val avatarLetter = when {
        !user?.username.isNullOrBlank() ->
            user!!.username!!.first().uppercaseChar().toString()
        !user?.email.isNullOrBlank() ->
            user!!.email.first().uppercaseChar().toString()
        else -> "?"
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFF1877F2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = avatarLetter,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SmallAvatarBubble(name: String) {
    val letter = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(Color(0xFF1877F2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

fun formatTimestamp(ts: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(ts))
}
