package com.umc.personal.data.retrofit.api

import com.umc.personal.data.dto.home.get.HomeItemDto
import com.umc.personal.data.dto.home.post.ProjectUploadDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ProjectAPI {

    @POST("/upload/project")
    @Headers("content-type: application/json")
    fun upload_projects(
        @Header("Authorization") accessToken: String,
        @Body uploadDto: ProjectUploadDto,
    ):Call<ResponseBody>
}