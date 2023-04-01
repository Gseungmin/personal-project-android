package com.umc.personal.data.dto.comment.post

import com.google.gson.annotations.SerializedName

/**
 * 댓글 작성 DTO
 * */
data class CommentPostDto(
    @SerializedName("projectId")
    var projectId: String?,
    @SerializedName("content")
    var content: String ?= null,
)