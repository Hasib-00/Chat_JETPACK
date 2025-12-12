package com.example.chat.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repo = AuthRepository()

    var email = ""
    var password = ""
    var repassword = ""

    var loading = false
    var registered = false
    var error: String? = null

    fun register(onResult: (Boolean) -> Unit) {
        if (loading) return

        if (password != repassword) {
            error = "Passwords do not match"
            onResult(false)
            return
        }

        viewModelScope.launch {
            loading = true
            error = null

            val success = repo.signUp(email, password)

            loading = false
            registered = success

            if (!success) error = "Registration failed"
            onResult(success)
        }
    }
}
