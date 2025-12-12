package com.example.chat.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repo = AuthRepository()

    var email = ""
    var password = ""

    var loading = false
    var loggedIn = false
    var error: String? = null

    fun login(onResult: (Boolean) -> Unit) {
        if (loading) return

        viewModelScope.launch {
            loading = true
            error = null

            val success = repo.signIn(email, password)

            loading = false
            loggedIn = success

            if (!success) error = "Login failed"
            onResult(success)
        }
    }
}
