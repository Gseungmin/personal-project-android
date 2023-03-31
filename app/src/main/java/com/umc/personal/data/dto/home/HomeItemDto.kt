package com.umc.personal.data.dto.home

import com.google.gson.annotations.SerializedName

/**
 * 결재 서류 목록 DTO
 * API 명세서 Check 완료
 * */
data class HomeItemDto(
    @SerializedName("content")
    val homeItem: List<HomeItem>
)
