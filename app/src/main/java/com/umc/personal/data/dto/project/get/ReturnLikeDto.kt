package com.umc.personal.data.dto.project.get

import com.google.gson.annotations.SerializedName


/**
 * 좋아요 Dto
 * */
data class ReturnLikeDto (
        @SerializedName("isLike")
        var isLike :Boolean,
)