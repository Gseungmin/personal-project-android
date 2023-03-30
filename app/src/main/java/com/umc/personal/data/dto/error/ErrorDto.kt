package com.umc.personal.data.dto.error

import com.google.gson.annotations.SerializedName


/**
 * 에러 Dto
 * */
data class ErrorDto (
        @SerializedName("code")
        var code : Int?=0,
        @SerializedName("message")
        var message : String?="",
)