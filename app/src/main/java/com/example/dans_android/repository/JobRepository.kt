package com.example.dans_android.repository

import android.util.Log
import com.example.dans_android.utils.RetrofitInstance
import com.example.dans_android.models.Jobs
import com.example.dans_android.service.ApiService
import retrofit2.HttpException

class JobRepository : ApiService {
    override suspend fun getPaginatedJobs(page: Int?, description: String?, location: String?, fullTime: Boolean?): List<Jobs?> {
        try {
            return RetrofitInstance.api.getPaginatedJobs(page, description, location).filterNotNull()
        } catch (e: HttpException) {
            // Handle specific HTTP 500 error or any HTTP error here
            if (e.code() == 500) {
                // Log or handle the error appropriately
                Log.e("API_ERROR", "HTTP 500 Internal Server Error occurred: ${e.message()}")
            }
            return emptyList()
        }
    }

    override suspend fun getJobById(id: String): Jobs {
        return RetrofitInstance.api.getJobById(id)
    }
}
