package com.example.icewarpdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.icewarpdemoapp.const.NavRoutes
import com.example.icewarpdemoapp.instance.RetrofitClient
import com.example.icewarpdemoapp.localDatabase.UserLocalDataSourceImpl
import com.example.icewarpdemoapp.ui.theme.IceWarpDemoAppTheme
import com.example.icewarpdemoapp.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IceWarpDemoAppTheme {

                val navController = rememberNavController()
                val context = this@MainActivity

                val userLocalDataSource = remember {
                    UserLocalDataSourceImpl(context)
                }

                val loginViewModel = remember {
                    LoginViewModel(
                        api = RetrofitClient.api,
                        localDataSource = userLocalDataSource
                    )
                }

                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.LOGIN
                ) {

                    composable(NavRoutes.LOGIN) {
                        LoginScreen(
                            viewModel = loginViewModel,
                            modifier = Modifier,
                            onLoginSuccess = {
                                navController.navigate(NavRoutes.CHANNELS) {
                                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(NavRoutes.CHANNELS) {
                        ChannelsScreen(
                            onLogoutConfirmed = {
                                userLocalDataSource.clearToken()
                                loginViewModel.resetLoginState()

                                navController.navigate(NavRoutes.LOGIN) {
                                    popUpTo(NavRoutes.CHANNELS) { inclusive = true }
                                }
                            },
                            userLocalDataSource = userLocalDataSource
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("testuser@mofa.onice.io") }
    var password by remember { mutableStateOf("Password123456") }
    val host = "mofa.onice.io"

    val state = viewModel.uiState

    // Navigate on success
    if (state.success) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE0E0E0)
            )
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // App icon (placeholder)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = Color(0xFF8E8CF7),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("IW", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sign in to IceWarp",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Input container
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = host,
                            onValueChange = {},
                            label = { Text("Host") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.login(username, password)
                    },
                    enabled = !state.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(if (state.loading) "Signing in..." else "Sign in")
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

