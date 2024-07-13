package com.example.dans_android.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dans_android.models.Jobs
import com.example.dans_android.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = JobRepository()

    private val _jobs = MutableStateFlow<List<Jobs>>(emptyList())
    val jobs: StateFlow<List<Jobs>> = _jobs

    private var currentPage = 1
    private var hasReachedMax = false
    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching

    private var currentDescription: String? = null
    private var currentLocation: String? = null
    private var currentFullTime: Boolean? = null

    init {
        fetchPaginatedJobs()
    }

    fun searchJobs(description: String?, location: String? = null, fullTime: Boolean? = null) {
        hasReachedMax = false
        currentPage = 1
        _jobs.value = emptyList()
        currentDescription = description
        currentLocation = location
        currentFullTime = fullTime
        fetchPaginatedJobs(description, location, fullTime)
    }

    private fun fetchPaginatedJobs(description: String? = null, location: String? = null, fullTime: Boolean? = null) {
        if (!_isFetching.value) {
            _isFetching.value = true
            viewModelScope.launch {
                try {
                    val jobs = repository.getPaginatedJobs(currentPage, description, location, fullTime).filterNotNull()
                    if (jobs.isEmpty()) {
                        hasReachedMax = true
                        _isFetching.value = false
                        return@launch
                    }
                    _jobs.value += jobs
                    currentPage++
                } catch (e: Exception) {
                    Log.e("fetchPaginatedJobs", "Error fetching jobs", e)
                } finally {
                    _isFetching.value = false
                }
            }
        }
    }

    fun loadMoreJobs() {
        if (!_isFetching.value && !hasReachedMax) {
            fetchPaginatedJobs(currentDescription, currentLocation, currentFullTime)
        }
    }
}

