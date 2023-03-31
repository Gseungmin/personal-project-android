package com.umc.personal.data.dto.home.post
import com.google.gson.annotations.SerializedName

/**
 * 프로젝트 업로드 DTO
 * */
data class ProjectUploadDto (
        @SerializedName("category")
        var category : Int? = null,
        @SerializedName("content")
        var content : String? = null,
        @SerializedName("title")
        var title : String? = null,
        @SerializedName("link")
        var opengraph : OpenGraphDto? = null,
        @SerializedName("image")
        var image : String? = null,
)