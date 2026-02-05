package com.example.icewarpdemoapp.viewModel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.icewarpdemoapp.interfacePackage.AuthApi
import com.example.icewarpdemoapp.localDatabase.UserLocalDataSource
import com.example.icewarpdemoapp.model.LoginUiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(
    private val api: AuthApi,
    private val localDataSource: UserLocalDataSource,
    private val TAG: String = "LoginViewModel"

) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(username: String, password: String) {

        // Validation
        when {
            username.isBlank() || password.isBlank() -> {
                uiState = uiState.copy(error = "Fields cannot be empty")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(username).matches() -> {
                uiState = uiState.copy(error = "Invalid email address")
                return
            }
            password.length < 6 -> {
                uiState = uiState.copy(error = "Password must be at least 6 characters")
                return
            }
        }

        uiState = uiState.copy(loading = true, error = null)

        api.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                localDataSource.saveToken(response.token)
                uiState = uiState.copy(loading = false, success = true)
                Log.d(TAG, "Login SUCCESS")
                Log.d(TAG, "Token: ${response.token}")

            }, { error ->
                uiState = uiState.copy(
                    loading = false,
                    error = error.message ?: "Login failed"
                )
            })
    }

    fun resetLoginState() {
        uiState = LoginUiState()
    }

}
