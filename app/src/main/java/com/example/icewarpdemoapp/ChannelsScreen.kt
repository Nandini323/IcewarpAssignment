package com.example.icewarpdemoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.icewarpdemoapp.const.ChannelsUiState
import com.example.icewarpdemoapp.instance.RetrofitClient
import com.example.icewarpdemoapp.localDatabase.UserLocalDataSource
import com.example.icewarpdemoapp.viewModel.ChannelsViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ChannelsScreen(
    onLogoutConfirmed: () -> Unit,
    userLocalDataSource: UserLocalDataSource
) {
    val viewModel = remember {
        ChannelsViewModel(
            api = RetrofitClient.channelsApi,
            localDataSource = userLocalDataSource
        )
    }
    // Dialog state
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Do you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        userLocalDataSource.clearToken()
                        onLogoutConfirmed()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back") },
                navigationIcon = {
                    IconButton(onClick =  { showLogoutDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        when (val state = viewModel.uiState) {

            //  Loading state
            ChannelsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            //  Error state
            is ChannelsUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }

            //  Success state (THIS IS THE IMPORTANT PART)
            is ChannelsUiState.Success -> {

                // Group channels by folder
                val groupedChannels = state.channels.groupBy {
                    it.groupFolderName ?: "Channels"
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    groupedChannels.forEach { (groupName, channels) ->

                        // Group Header
                        item {
                            Text(
                                text = groupName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE0E0E0))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        // Channel items
                        items(channels) { channel ->
                            Text(
                                text = channel.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
