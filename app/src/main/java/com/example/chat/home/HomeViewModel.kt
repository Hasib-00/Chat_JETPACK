package com.example.chat.home

import androidx.lifecycle.ViewModel
import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val currentUserId = auth.currentUser?.uid

                val users = snapshot.documents
                    .mapNotNull { it.toObject(UserModel::class.java) }
                    .filter { it.id != currentUserId }

                _state.value = HomeState(
                    loading = false,
                    users = users
                )
            }
            .addOnFailureListener {
                _state.value = HomeState(loading = false)
            }
    }
}
