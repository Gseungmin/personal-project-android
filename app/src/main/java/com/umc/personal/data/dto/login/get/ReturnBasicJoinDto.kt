package com.umc.personal.data.dto.login.get

import com.google.gson.annotations.SerializedName


/**
 * 일반 회원가입시 Dto
 * */
data class ReturnBasicJoinDto (
        @SerializedName("email")
        var email : String?="",
        @SerializedName("isTrue")
        var isTrue : Boolean?=false,
)