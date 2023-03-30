package com.umc.personal.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.personal.data.dto.error.ErrorDto
import com.umc.personal.data.dto.login.get.ReturnBasicJoinDto
import com.umc.personal.data.dto.login.post.BasicJoinDto
import com.umc.personal.data.repository.login.LoginFragmentRepository
import com.umc.personal.dataStore.AccessTokenDataStore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Email Check ViewModel
 * */
class JoinViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()

    private var _join_state = MutableLiveData<Boolean>()
    val join_state : LiveData<Boolean>
        get() = _join_state

    //에러 라이브 데이터
    private var _error = MutableLiveData<ErrorDto>()
    val error : LiveData<ErrorDto>
        get() = _error

    /**
     * 일반 회원가입
     * 정상 동작 Check 완료
     * */
    fun join(basicJoinDto: BasicJoinDto) = viewModelScope.launch {

        val response = repository.basic_join(basicJoinDto)
        response.enqueue(object : Callback<ReturnBasicJoinDto> {
            override fun onResponse(call: Call<ReturnBasicJoinDto>, response: Response<ReturnBasicJoinDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _join_state.postValue(response.body()?.isTrue ?: false)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnBasicJoinDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}