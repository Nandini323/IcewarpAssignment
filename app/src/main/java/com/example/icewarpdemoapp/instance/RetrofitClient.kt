package com.example.icewarpdemoapp.instance

import com.example.icewarpdemoapp.interfacePackage.AuthApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import com.example.icewarpdemoapp.interfacePackage.ChannelsApi

object RetrofitClient {

    private const val BASE_URL = "https://mofa.onice.io/teamchatapi/"

    // 🔹 ONE Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // Needed ONLY because AuthApi uses RxJava
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    // 🔹 RxJava API (Login)
    val api: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    // 🔹 Coroutine API (Channels)
    val channelsApi: ChannelsApi by lazy {
        retrofit.create(ChannelsApi::class.java)
    }
}
