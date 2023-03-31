package com.umc.personal.data.dto.home.get

import com.google.gson.annotations.SerializedName

//프로젝트 목록 DTO
data class HomeItemDto(
    @SerializedName("content")
    val homeItem: List<HomeItem>
)
