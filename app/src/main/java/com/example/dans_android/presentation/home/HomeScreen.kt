package com.example.dans_android.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dans_android.models.Jobs
import com.example.dans_android.presentation.routes.Screen

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }
    val jobs by homeViewModel.jobs.collectAsState()
    val isFetching by homeViewModel.isFetching.collectAsState()
    var fullTimeFilter by remember { mutableStateOf(false) }
    var locationFilter by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(searchText) { searchText = it }
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Full-time", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = fullTimeFilter,
                onCheckedChange = { fullTimeFilter = it }
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = locationFilter,
                onValueChange = { locationFilter = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Location...") },
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                homeViewModel.searchJobs(searchText, locationFilter.takeIf { it.isNotBlank() }, fullTimeFilter.takeIf { it })
            }) {
                Text("Apply Filters")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        JobList(navController, jobs, homeViewModel, isFetching)
    }

    LaunchedEffect(searchText) {
        homeViewModel.searchJobs(searchText)
    }
}


@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = { onSearchTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search...") },
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun JobList(navController: NavController, jobs: List<Jobs>, homeViewModel: HomeViewModel, isFetching: Boolean) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (jobs.isEmpty() && !isFetching) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No jobs found", style = MaterialTheme.typography.body1)
                    }
                }
            } else {
                itemsIndexed(jobs) { _, job ->
                    JobItem(navController, job = job)
                }
            }
        }

        if (isFetching) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background.copy(alpha = 0.8f)), // Semi-transparent background
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !isFetching) {
            homeViewModel.loadMoreJobs()
        }
    }
}

@Composable
fun JobItem(navController: NavController, job: Jobs) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("${Screen.JobDetail.route}/${job.id}") }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = job.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = job.company,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = job.location,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}

