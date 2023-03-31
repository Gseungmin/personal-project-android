package com.umc.personal.data.repository.project

import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.personal.data.dto.project.get.ProjectDto
import com.umc.personal.data.dto.project.get.ReturnLikeDto
import com.umc.personal.data.retrofit.instance.RetrofitInstance.projectAPI
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project Activity Repository
 * */
class ProjectActivityRepository() {

    //like API
    fun like(accessToken: String, projectId: String): Call<ReturnLikeDto> {
        return projectAPI.like(accessToken, projectId)
    }

    //댓글 API
    fun getComments(projectId: String): Call<CommentListDto> {
        return projectAPI.getComments(projectId)
    }

    //댓글 작성 API
    fun postComments(accessToken: String, commentPostDto: CommentPostDto): Call<ResponseBody> {
        return projectAPI.postComment(accessToken, commentPostDto)
    }

    //프로젝트 API
    fun getProject(accessToken:String, projectId:String): Call<ProjectDto> {
        return projectAPI.getProject(accessToken, projectId)
    }
}