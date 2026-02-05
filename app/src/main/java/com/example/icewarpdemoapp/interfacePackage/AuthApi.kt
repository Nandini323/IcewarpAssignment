package com.example.icewarpdemoapp.interfacePackage

import com.example.icewarpdemoapp.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("iwauthentication.login.plain")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): io.reactivex.rxjava3.core.Single<LoginResponse>
}
