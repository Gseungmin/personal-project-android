package com.umc.personal.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.personal.data.dto.login.get.ReturnBasicJoinDto
import com.umc.personal.data.dto.login.post.BasicJoinDto
import com.umc.personal.data.dto.login.post.BasicLoginDto
import com.umc.personal.data.repository.login.LoginFragmentRepository
import com.umc.personal.data.repository.token.AccessTokenRepository
import com.umc.personal.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

/**
 * Email Check ViewModel
 * */
class LoginViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()

    private var _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    private var _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean>
        get() = _success

    private var _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess : LiveData<Boolean>
        get() = _logoutSuccess

    //로그인 시 엑세스 토큰 저장
    fun setAccessToken(token : String) = viewModelScope.launch {
        AccessTokenDataStore().setAccessToken(token)
    }

    //엑세스 토큰 삭제 메소드
    fun deleteAccessToken() = viewModelScope.launch {
        AccessTokenDataStore().deleteAccessToken()
    }

    //일반 로그인 성공시 엑세스 토큰 발급 및 저장
    fun basic_login(basicLoginDto: BasicLoginDto) = viewModelScope.launch {
        val response = repository.basic_login(basicLoginDto)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _accessToken.value = response.headers().get("Authorization").toString()
                    setAccessToken(accessToken.value.toString())
                    _success.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    _success.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    //카카오 로그인 성공시 엑세스 토큰 발급 및 저장
    fun kako_login(token : String) = viewModelScope.launch {
        val response = repository.kakao_login(token)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _accessToken.value = response.headers().get("Authorization").toString()
                    setAccessToken(accessToken.value.toString())
                    _success.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    _success.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 로그아웃
     * */
    fun logout() = viewModelScope.launch {

        val tokenValue = AccessTokenDataStore().getAccessToken().first()

        val response = repository.logout(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    deleteAccessToken()
                    _logoutSuccess.postValue(true)
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