package com.example.icewarpdemoapp.model

data class ChannelsResponse(
    val channels: List<Channel>
)

data class Channel(
    val id: String,
    val name: String,
    val groupFolderName: String?
)
