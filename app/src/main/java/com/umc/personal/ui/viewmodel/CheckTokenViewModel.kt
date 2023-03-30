package com.umc.personal.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.personal.data.dto.error.ErrorDto
import com.umc.personal.data.repository.token.AccessTokenRepository
import com.umc.personal.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Email Check ViewModel
 * */
class CheckTokenViewModel() : ViewModel() {

    //엑세스 토큰 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    //엑세스 토큰 라이브데이터
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    //에러 라이브 데이터
    private var _error = MutableLiveData<ErrorDto>()
    val error : LiveData<ErrorDto>
        get() = _error

    /**
     * 로그인 상태 체크 API
     * 정상 동작 Check 완료
     * */
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()
        val response = accessTokenRepository.checkAccessToken(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _accessToken.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(ErrorDto(
                        jsonObject.getInt("code"), jsonObject.getString("message")))
                    _accessToken.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
                _accessToken.postValue(false)
            }
        })
    }
}