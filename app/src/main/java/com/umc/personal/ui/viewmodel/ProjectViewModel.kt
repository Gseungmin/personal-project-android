package com.umc.personal.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.personal.data.dto.comment.get.CommentListDto
import com.umc.personal.data.dto.comment.post.CommentPostDto
import com.umc.personal.data.dto.project.get.ProjectDto
import com.umc.personal.data.dto.project.get.ReturnLikeDto
import com.umc.personal.data.repository.project.ProjectActivityRepository
import com.umc.personal.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//프로젝트 ViewModel
class ProjectViewModel() : ViewModel() {

    //리포지토리
    private val repository = ProjectActivityRepository()

    //서버에서 받아올 프로젝트 데이터
    private var _project = MutableLiveData<ProjectDto>()
    val project : LiveData<ProjectDto>
        get() = _project

    //좋아요
    private var _like = MutableLiveData<Boolean>()
    val like : LiveData<Boolean>
        get() = _like

    //서버에서 받아올 댓글 데이터
    private var _comments = MutableLiveData<CommentListDto>()
    val comments : LiveData<CommentListDto>
        get() = _comments

    //좋아요 셋팅
    fun setLike(isLiked: Boolean) = viewModelScope.launch {
        _like.postValue(isLiked)
    }

    //프로젝트 API 호출
    fun get_project_detail(projectId: String) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.getProject(accessToken, projectId)
        response.enqueue(object : Callback<ProjectDto> {
            override fun onResponse(call: Call<ProjectDto>, response: Response<ProjectDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _project.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProjectDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }


    //좋아요 API 호출
    fun like(projectId: String) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.like(accessToken, projectId)
        response.enqueue(object : Callback<ReturnLikeDto> {
            override fun onResponse(call: Call<ReturnLikeDto>, response: Response<ReturnLikeDto>) {
                if (response.isSuccessful) {
                    _like.postValue(response.body()!!.isLike)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnLikeDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 모든 comments 목록을 API
     * */
    fun get_comments(projectId: String) = viewModelScope.launch {

        val response = repository.getComments(projectId)
        response.enqueue(object : Callback<CommentListDto> {
            override fun onResponse(call: Call<CommentListDto>, response: Response<CommentListDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _comments.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommentListDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 댓글 작성 API
     * */
    fun post_comments(commentPostDto: CommentPostDto) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.postComments(accessToken, commentPostDto)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    get_comments(commentPostDto.projectId.toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}