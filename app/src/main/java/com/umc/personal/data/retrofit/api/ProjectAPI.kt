package com.umc.personal.data.retrofit.api

import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.personal.data.dto.home.post.ProjectUploadDto
import com.umc.personal.data.dto.project.get.ProjectDto
import com.umc.personal.data.dto.project.get.ReturnLikeDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ProjectAPI {

    //게시물 업로드 API
    @POST("/upload/project")
    @Headers("content-type: application/json")
    fun upload_projects(
        @Header("Authorization") accessToken: String,
        @Body uploadDto: ProjectUploadDto,
    ):Call<ResponseBody>

    //게시물 좋아요 API
    @POST("/likes/{projectId}")
    @Headers("content-type: application/json")
    fun like(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId:String,
    ):Call<ReturnLikeDto>

    //댓글 목록 API
    @GET("/comments/{projectId}")
    @Headers("content-type: application/json")
    fun getComments(@Path("projectId") projectId: String): Call<CommentListDto>

    //댓글 작성 API
    @POST("/comments")
    @Headers("content-type: application/json")
    fun postComment(@Header("Authorization") accessToken: String, @Body commentPostDto: CommentPostDto): Call<ResponseBody>

    //댓글 목록 API
    @GET("/project/{projectId}")
    @Headers("content-type: application/json")
    fun getProject(@Header("Authorization") accessToken: String, @Path("projectId") projectId: String): Call<ProjectDto>
}