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
        @SerializedName("linkTitle")
        var linkTitle : String? = null,
        @SerializedName("linkUrl")
        var linkUrl : String? = null,
        @SerializedName("linkImage")
        var linkImage : String? = null,
        @SerializedName("image")
        var image : String? = null,
)