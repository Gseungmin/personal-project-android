package com.umc.personal.data.retrofit.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Access Token 검증 API
 * */
interface AccessTokenAPI {

    @GET("/token/check")
    @Headers("content-type: application/json")
    fun checkAccessToken(
        @Header("Authorization") accessToken: String
    ): Call<ResponseBody>
}