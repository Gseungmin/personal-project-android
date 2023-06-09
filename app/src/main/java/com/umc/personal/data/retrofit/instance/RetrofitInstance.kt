package com.umc.personal.data.retrofit.instance

import com.google.gson.GsonBuilder
import com.umc.personal.API.LOCAL_BASE_URL
import com.umc.personal.data.retrofit.api.AccessTokenAPI
import com.umc.personal.data.retrofit.api.LoginAPI
import com.umc.personal.data.retrofit.api.ProjectAPI
import com.umc.personal.data.retrofit.api.SearchAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val okHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val retrofit: Retrofit by lazy {

        val gson = GsonBuilder().setLenient().create()

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).baseUrl(LOCAL_BASE_URL).build() //build로 객체 생성
    }

    /**login api*/
    val loginApi: LoginAPI by lazy {
        retrofit.create(LoginAPI::class.java)
    }

    /**token api*/
    val accessTokenAPI: AccessTokenAPI by lazy {
        retrofit.create(AccessTokenAPI::class.java)
    }

    /**search api*/
    val searchAPI: SearchAPI by lazy {
        retrofit.create(SearchAPI::class.java)
    }

    /**upload api*/
    val projectAPI: ProjectAPI by lazy {
        retrofit.create(ProjectAPI::class.java)
    }
}
