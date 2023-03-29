package com.umc.personal.data.repository.token
import com.umc.personal.data.retrofit.instance.RetrofitInstance.accessTokenAPI
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * AccessToken Check Repository
 * 구현 완료
 * */
class AccessTokenRepository {

    fun checkAccessToken(accessToken: String): Call<ResponseBody> {
        return accessTokenAPI.checkAccessToken(accessToken)
    }
}