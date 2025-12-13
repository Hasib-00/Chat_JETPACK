package com.example.chat.Profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.auth.AuthRepository
import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {


    private val repo = AuthRepository()

    var user by mutableStateOf<UserModel?>(null)
        private set

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            user = repo.getCurrentUser()
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update("username", newUsername)
        }
    }
}
