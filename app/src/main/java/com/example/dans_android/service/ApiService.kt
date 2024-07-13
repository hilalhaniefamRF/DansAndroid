package com.example.dans_android.service

import com.example.dans_android.models.Jobs
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("positions.json")
    suspend fun getPaginatedJobs(
        @Query("page") page: Int? = 1,
        @Query("description") description: String? = "",
        @Query("location") location: String? = "",
        @Query("full_time") fullTime: Boolean? = false
    ): List<Jobs?>

    @GET("positions/{id}")
    suspend fun getJobById(@Path("id") id: String): Jobs
}
