package com.example.chat.home

import com.example.chat.model.UserModel

data class HomeState(
    val users: List<UserModel> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
