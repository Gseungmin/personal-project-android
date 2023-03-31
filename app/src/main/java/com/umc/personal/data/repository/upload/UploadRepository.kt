package com.umc.personal.data.repository.upload

import com.umc.personal.data.dto.home.post.ProjectUploadDto
import com.umc.personal.data.retrofit.instance.RetrofitInstance.projectAPI
import okhttp3.ResponseBody
import retrofit2.Call

//Upload Repository
class UploadRepository() {

    //upload
    fun upload(accessToken: String, uploadDto: ProjectUploadDto): Call<ResponseBody> {
        return projectAPI.upload_projects(accessToken, uploadDto)
    }
}