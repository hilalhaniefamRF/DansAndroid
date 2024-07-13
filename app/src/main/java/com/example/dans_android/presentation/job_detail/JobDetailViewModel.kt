package com.example.dans_android.presentation.job_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dans_android.models.Jobs
import com.example.dans_android.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobDetailViewModel : ViewModel() {
    private val repository = JobRepository()

    private val _jobDetail = MutableStateFlow<Jobs?>(null)
    val jobDetail: StateFlow<Jobs?> = _jobDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun fetchJobDetail(jobId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val job = repository.getJobById(jobId)
                _jobDetail.value = job
            } catch (e: Exception) {
                _errorState.value = "Failed to fetch job details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

