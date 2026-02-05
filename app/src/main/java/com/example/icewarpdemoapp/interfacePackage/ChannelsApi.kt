package com.example.icewarpdemoapp.interfacePackage

import com.example.icewarpdemoapp.model.ChannelsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChannelsApi {

    @FormUrlEncoded
    @POST("channels.list")
    suspend fun getChannels(
        @Field("token") token: String,
        @Field("include_unread_count") includeUnread: Boolean = true,
        @Field("exclude_members") excludeMembers: Boolean = true,
        @Field("include_permissions") includePermissions: Boolean = false
    ): ChannelsResponse
}
