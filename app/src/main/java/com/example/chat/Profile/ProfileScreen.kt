package com.example.chat.Profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.auth.AnimatedTextField
import com.example.chat.home.HomeViewModel



@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    vm: ProfileViewModel = viewModel()
)
{

    var username by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(Modifier.height(100.dp))

        AnimatedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            placeholder = "Enter your Password",
            isPassword = false
        )

        Button(
            onClick = {
                vm.updateUsername(username)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Login", color = Color.White)
        }


    }
}