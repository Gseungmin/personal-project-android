package com.umc.personal.data.dto.home.post

import com.google.gson.annotations.SerializedName

/**open graph dto*/
data class OpenGraphDto(

    @SerializedName("url")
    var url: String ? = "",
    @SerializedName("title")
    var title: String ? = "",
    @SerializedName("image")
    var image: String ? = ""
)
