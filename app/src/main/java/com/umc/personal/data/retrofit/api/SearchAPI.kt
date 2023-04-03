package com.umc.personal.data.retrofit.api

import com.umc.personal.data.dto.home.get.HomeItemDto
import retrofit2.Call
import retrofit2.http.*

interface SearchAPI {

    @GET("/search")
    @Headers("content-type: application/json")
    fun search_projects(
        @Query("query") query: String?="",
        @Query("sort") sort: Int?=0,
    ):Call<HomeItemDto>
}