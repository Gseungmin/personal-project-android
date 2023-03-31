package com.umc.personal.data.repository.home

import com.umc.personal.data.dto.home.HomeItemDto
import com.umc.personal.data.dto.login.get.*
import com.umc.personal.data.dto.login.post.*
import com.umc.personal.data.retrofit.instance.RetrofitInstance.loginApi
import com.umc.personal.data.retrofit.instance.RetrofitInstance.searchAPI
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Home Fragment Repository
 * */
class HomeFragmentRepository() {

    //search API
    fun search(query: String): Call<HomeItemDto> {
        return searchAPI.search_projects(query)
    }
}