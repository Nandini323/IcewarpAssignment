package com.example.icewarpdemoapp.const

import com.example.icewarpdemoapp.model.Channel

sealed class ChannelsUiState {
    object Loading : ChannelsUiState()
    data class Success(val channels: List<Channel>) : ChannelsUiState()
    data class Error(val message: String) : ChannelsUiState()
}
