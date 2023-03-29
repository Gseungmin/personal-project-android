package com.umc.personal.data.retrofit.api

import com.umc.personal.data.dto.login.get.ReturnBasicJoinDto
import com.umc.personal.data.dto.login.post.BasicJoinDto
import com.umc.personal.data.dto.login.post.BasicLoginDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * 로그인 및 회원가입 관련 기능 API
 * */
interface LoginAPI {

    /**
     * 일반 회원가입 API
     * API 명세서 Check 완료
     * */
    @POST("/auth/join")
    @Headers("content-type: application/json")
    fun basic_join(@Body basicJoinDto: BasicJoinDto):Call<ReturnBasicJoinDto>

    /**
     * @Post
     * Header LoginCase : basic
     * BasicLoginDto
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/login")
    @Headers("content-type: application/json")
    fun basic_login(@Header("LoginCase") case: String, basicLoginDto: BasicLoginDto):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization : Bearer accessToken
     * Header LoginCase : google, kakao
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/login")
    @Headers("content-type: application/json")
    fun social_login(@Header("Authorization") accessToken:
              String, @Header("LoginCase") case: String):Call<ResponseBody>
}