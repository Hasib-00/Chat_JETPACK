package com.example.chat.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chat.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    vm: LoginViewModel = viewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent, // important!
            contentColor = Color.Black
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
                    text = "Login",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(200.dp))

                AnimatedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        vm.email = it
                    },
                    placeholder = "Enter your Email"
                )

                Spacer(Modifier.height(20.dp))

                AnimatedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        vm.password = it
                    },
                    placeholder = "Enter your Password",
                    isPassword = true
                )

                Spacer(Modifier.height(90.dp))

                Button(
                    onClick = {
                        vm.login { success ->
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (success) "Login Successful!" else vm.error ?: "Login failed"
                                )
                            }
                            if (success) navController.navigate("Home")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(46.dp)
                        .padding(horizontal = 25.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Login", color = Color.White, fontSize = 17.sp)
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don't have an account? ")
                    Text(
                        "Register",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("Register") }
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

@Composable
fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color.Red
            isFocused -> MaterialTheme.colorScheme.primary
            else -> Color.LightGray
        },
        label = "borderColorAnim"
    )

    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            },
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            isError = isError
        )
    }
}