package com.umc.personal.data.repository.login

import com.umc.personal.data.dto.login.get.*
import com.umc.personal.data.dto.login.post.*
import com.umc.personal.data.retrofit.instance.RetrofitInstance.loginApi
import retrofit2.Call

/**
 * Login Fragment Repository
 * */
class LoginFragmentRepository() {

    /**
     * Basic Join API
     * */
    fun basic_join(user : BasicJoinDto): Call<ReturnBasicJoinDto> {
        return loginApi.basic_join(user)
    }
}