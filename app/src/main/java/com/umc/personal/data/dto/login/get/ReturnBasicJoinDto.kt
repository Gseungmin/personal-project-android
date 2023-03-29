package com.umc.personal.data.dto.login.get

import com.google.gson.annotations.SerializedName


/**
 * 일반 회원가입시 Dto
 * API 명세서 Check 완료
 * */
data class ReturnBasicJoinDto (
        @SerializedName("email")
        var email : String?="",
        @SerializedName("isTrue")
        var isTrue : Boolean?=false,
)