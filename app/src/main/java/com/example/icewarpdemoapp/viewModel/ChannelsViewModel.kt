package com.example.icewarpdemoapp.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icewarpdemoapp.const.ChannelsUiState
import com.example.icewarpdemoapp.interfacePackage.ChannelsApi
import com.example.icewarpdemoapp.localDatabase.UserLocalDataSource
import kotlinx.coroutines.launch

class ChannelsViewModel(
    private val api: ChannelsApi,
    private val localDataSource: UserLocalDataSource
) : ViewModel() {

    var uiState by mutableStateOf<ChannelsUiState>(ChannelsUiState.Loading)
        private set

    init {
        loadChannels()
    }

    private fun loadChannels() {
        viewModelScope.launch {
            try {
                val token = localDataSource.getToken()
                    ?: throw IllegalStateException("Token not found")

                val response = api.getChannels(token)

                // save to DB later (SQLDelight hook)
                uiState = ChannelsUiState.Success(response.channels)

            } catch (e: Exception) {
                uiState = ChannelsUiState.Error(e.message ?: "Error loading channels")
            }
        }
    }
}
