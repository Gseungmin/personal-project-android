package com.umc.personal.data.retrofit.api

import com.umc.personal.data.dto.login.get.ReturnBasicJoinDto
import com.umc.personal.data.dto.login.post.BasicJoinDto
import retrofit2.Call
import retrofit2.http.Body
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
}