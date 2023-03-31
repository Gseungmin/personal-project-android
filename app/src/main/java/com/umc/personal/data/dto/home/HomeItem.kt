package com.umc.personal.data.dto.home

import com.google.gson.annotations.SerializedName

/**
 * 프로젝트 목록 DTO
 * */
data class HomeItem(
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("thumbnailImage")
    val thumbnailImage: String,
    @SerializedName("state")
    val state: Int,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("view")
    val view: Int,
)