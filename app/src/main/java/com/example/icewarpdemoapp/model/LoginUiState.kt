package com.example.icewarpdemoapp.model

data class LoginUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
