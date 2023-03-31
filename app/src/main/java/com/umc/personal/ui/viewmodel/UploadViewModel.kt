package com.umc.personal.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.personal.data.dto.home.post.OpenGraphDto
import com.umc.personal.data.dto.home.post.ProjectUploadDto
import com.umc.personal.data.repository.upload.UploadRepository
import com.umc.personal.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//프로젝트 ViewModel
class UploadViewModel() : ViewModel() {

    //리파지토리
    private val repository = UploadRepository()

    //카테고리 라이브데이터
    private var _category = MutableLiveData<Int>()
    val category : LiveData<Int>
        get() = _category

    //선택된 이미지 라이브데이터
    private var _pic = MutableLiveData<Uri>()
    val pic : LiveData<Uri>
        get() = _pic

    //이미지 저장경로 라이브데이터
    private var _image = MutableLiveData<String>()
    val image : LiveData<String>
        get() = _image

    //오픈 그래프 라이브데이터
    private var _opengraph = MutableLiveData<OpenGraphDto>()
    val opengraph : LiveData<OpenGraphDto>
        get() = _opengraph

    //링크 라이브데이터
    private var _link = MutableLiveData<String>()
    val link : LiveData<String>
        get() = _link

    /**image 선택시 적용*/
    fun setImage(image: Uri) {
        _pic.postValue(image)
    }

    /**link가 올바른 경우 Opengraph 적용*/
    fun setOpengraph(og: OpenGraphDto) {
        _opengraph.postValue(og)
    }

    /**link 선택시 적용*/
    fun setLink(li: String) {
        _link.postValue(li)
    }

    /**images 선택시 적용*/
    fun setImages(image: String) {
        _image.postValue(image)
    }

    /**카테고리 선택시 적용*/
    fun setCategory(cate: Int) {
        _category.postValue(cate)
    }

    //프로젝트를 등록
    fun post_project(upload: ProjectUploadDto) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.upload(accessToken, upload)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
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