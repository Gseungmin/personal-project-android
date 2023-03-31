package com.umc.approval.data.dto.comment.get

import com.google.gson.annotations.SerializedName

/**
 * 개별 댓글 DTO
 * */
data class CommentDto(
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("commentId")
    val commentId: Int,
)