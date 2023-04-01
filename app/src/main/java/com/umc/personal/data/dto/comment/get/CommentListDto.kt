package com.umc.personal.data.dto.comment.get

import com.google.gson.annotations.SerializedName

/**
 * 댓글 목록 DTO
 * */
data class CommentListDto(
    @SerializedName("content")
    val content: List<CommentDto>
)