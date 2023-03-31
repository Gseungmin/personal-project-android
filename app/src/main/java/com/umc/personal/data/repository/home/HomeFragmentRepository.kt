package com.umc.personal.data.repository.home

import com.umc.personal.data.dto.home.get.HomeItemDto
import com.umc.personal.data.retrofit.instance.RetrofitInstance.searchAPI
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