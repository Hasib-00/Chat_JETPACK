package com.example.chat.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chat.R
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    vm: RegisterViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🔥 Full screen background here
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))   // ← change to any color
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent  // IMPORTANT: keeps background visible
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(Modifier.height(40.dp))

                Text(
                    "Register",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(160.dp))

                // Email field
                AnimatedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        vm.email = it
                    },
                    placeholder = "Enter your Email"
                )

                Spacer(Modifier.height(20.dp))

                // Password field
                AnimatedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        vm.password = it
                    },
                    placeholder = "Enter your Password",
                    isPassword = true
                )

                Spacer(Modifier.height(20.dp))

                // Re-password field
                AnimatedTextField(
                    value = repassword,
                    onValueChange = {
                        repassword = it
                        vm.repassword = it
                    },
                    placeholder = "Re-enter your Password",
                    isPassword = true
                )

                Spacer(Modifier.height(55.dp))

                // REGISTER BUTTON
                Button(
                    onClick = {
                        vm.register { success ->
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (success) "Registration Successful!" else vm.error ?: "Failed"
                                )
                            }
                            if (success) navController.navigate("Login")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .padding(horizontal = 25.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("Register", color = Color.White, fontSize = 17.sp)
                }

                Spacer(Modifier.height(20.dp))

                // Navigation to Login
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Already have an account? ")
                    Text(
                        "Login",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("Login") }
                    )
                }
                Spacer(Modifier.height(50.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xD5E1E1E1))
                ) {

                    Icon(
                        painter = painterResource(R.drawable.google),
                        contentDescription = "Google",
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Sign in with Google",
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.height(30.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xD5E1E1E1))
                ) {

                    Icon(
                        painter = painterResource(R.drawable.apple),
                        contentDescription = "apple",
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Sign in with apple",
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
