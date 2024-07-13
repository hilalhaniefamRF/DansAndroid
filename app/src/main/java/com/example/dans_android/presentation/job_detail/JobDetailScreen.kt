package com.example.dans_android.presentation.job_detail

import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment

@Composable
fun JobDetailScreen(
    jobId: String,
    jobDetailViewModel: JobDetailViewModel = viewModel()
) {
    val jobDetail by jobDetailViewModel.jobDetail.collectAsState()
    val isLoading by jobDetailViewModel.isLoading.collectAsState()
    val errorState by jobDetailViewModel.errorState.collectAsState()

    LaunchedEffect(jobId) {
        jobDetailViewModel.fetchJobDetail(jobId)
    }

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (errorState != null) {
                Text(text = "Error: ${errorState!!}")
            } else {
                jobDetail?.let { detail ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        Text(
                            text = detail.title,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Company: ${detail.company}",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Location: ${detail.location}",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = detail.type,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        detail.description.let { description ->
                            AndroidView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(vertical = 8.dp),
                                factory = { context ->
                                    WebView(context).apply {
                                        settings.loadsImagesAutomatically = true
                                        settings.javaScriptEnabled = false
                                        settings.domStorageEnabled = true
                                        webViewClient = object : WebViewClient() {
                                            override fun shouldOverrideUrlLoading(
                                                view: WebView?,
                                                request: WebResourceRequest?
                                            ): Boolean {
                                                // Handle clicks on URLs (open in external browser)
                                                val url = request?.url.toString()
                                                view?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                                return true
                                            }
                                        }
                                        loadDataWithBaseURL(null, description, "text/html", "utf-8", null)
                                    }
                                }
                            )
                        }
                    }
                } ?: Text(text = "Job details not available")
            }
        }
    }
}

