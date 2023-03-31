package com.umc.personal.data.dto.home

import com.google.gson.annotations.SerializedName
/**
 * 목록에서 결재 서류 DTO
 * API 명세서 Check 완료
 * */
data class HomeItem(
    @SerializedName("documentId")
    val documentId: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("thumbnailImage")
    val thumbnailImage: String,
    @SerializedName("imageCount")
    val imageCount: Int,
    @SerializedName("state")
    val state: Int,
    @SerializedName("approvalCount")
    val approvalCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("view")
    val view: Int,
)