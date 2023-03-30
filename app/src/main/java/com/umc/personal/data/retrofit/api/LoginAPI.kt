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
import retrofit2.http.Query

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
     * email, password
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/auth/login")
    @Headers("content-type: application/json")
    fun basic_login(@Query("email") email: String,
                    @Query("password") password: String):Call<ResponseBody>

    /**
     * @Post
     * Access Token
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/auth/kakao")
    @Headers("content-type: application/json")
    fun kakao_login(@Header("Authorization") accessToken: String):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization : Bearer accessToken
     * */
    @POST("/auth/logout")
    @Headers("content-type: application/json")
    fun logout(@Header("Authorization") accessToken : String):Call<ResponseBody>
}