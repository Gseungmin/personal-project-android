package com.umc.personal.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.personal.data.dto.home.get.HomeItemDto
import com.umc.personal.data.repository.home.HomeFragmentRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//홈 화면 ViewModel
class HomeViewModel() : ViewModel() {

    //검색어 라이브데이터
    private var _query = MutableLiveData<String>()
    val query : LiveData<String>
        get() = _query

    //반환 목록 라이브데이터
    private var _all_list = MutableLiveData<HomeItemDto>()
    val all_list : LiveData<HomeItemDto>
        get() = _all_list

    //API 리포지토리
    private val repository = HomeFragmentRepository()

    //검색어 쿼리
    fun setQuery(query: String) = viewModelScope.launch {
        _query.postValue(query)
    }

    //모든 프로젝트 목록을 반환받는 API
    fun get_all_preject(query: String?="", sort: Int?=0) = viewModelScope.launch {
        val response = repository.search(query, sort)
        response.enqueue(object : Callback<HomeItemDto> {
            override fun onResponse(call: Call<HomeItemDto>, response: Response<HomeItemDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _all_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<HomeItemDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}