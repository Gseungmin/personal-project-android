package com.umc.personal.data.dto.project.get

import com.google.gson.annotations.SerializedName
import com.umc.personal.data.dto.home.post.OpenGraphDto


/**
 * 프로젝트 DTO
 * */
data class ProjectDto (
        @SerializedName("projectId")
        val projectId: Int,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("profileImage")
        val profileImage: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("likeCount")
        val likeCount: Int,
        @SerializedName("commentCount")
        val commentCount: Int,
        @SerializedName("view")
        val view: Int,
        @SerializedName("isLiked")
        val isLiked: Boolean,
        @SerializedName("link")
        val link: OpenGraphDto,
)