package com.example.composesubmission.ui.screen.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.androidintermedieatesubmission.data.response.Rumah
import com.example.composesubmission.R
import com.example.composesubmission.ui.components.SearchBarView
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRumahScreen(
    rumahViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    navigateToTambah: () -> Unit,
    navigateBackResult: Boolean,
    
) {

    val context = LocalContext.current

    val query by rumahViewModel.query

    val uiList by rumahViewModel.uiList.collectAsState()
    val loading by rumahViewModel.loading.collectAsState()

    var isRefresh by  rumahViewModel.isRefresh



    if (!rumahViewModel.error.value.isNullOrEmpty()) {
        Toast.makeText(context, rumahViewModel.error.value, Toast.LENGTH_SHORT).show()
    }

    if (navigateBackResult && !isRefresh){
        rumahViewModel.getRumahCloud()
        isRefresh = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Properti Ideal")
                },)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(

                onClick = {
                    navigateToTambah()
                },
                icon = { Icon(Icons.Filled.Add, "Tambah Data") },
                text = { Text(text = "Tambah") },
            )
        }
    ) {
        if (!loading) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                val scope = rememberCoroutineScope()
                val listState = rememberLazyListState()
                val showButton: Boolean by remember {
                    derivedStateOf { listState.firstVisibleItemIndex > 0 }
                }

                SearchBarView(
                    query = query,
                    onQueryChange = rumahViewModel::searchHeroes,
                    modifier = modifier
                )

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = 80.dp, top = 80.dp)
                ) {

                    items(uiList, key = { it.id!! }) {
                        RumahListItem(
                            dataRumah = it,
                            modifier = modifier.clickable {
                                navigateToDetail(it.id!!)
                            }
                        )
                    }
                }


                AnimatedVisibility(
                    visible = showButton,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = modifier
                        .padding(bottom = 30.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    ScrollToTopButton(
                        onClick = {
                            scope.launch {
                                listState.scrollToItem(index = 0)
                            }
                        }
                    )
                }

            }

        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }

        }
    }



}

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = stringResource(R.string.scroll_to_top),
        )
    }
}

@Composable
fun RumahListItem(
    dataRumah: Rumah,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(200.dp)
    ) {

        AsyncImage(
            model = dataRumah.photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(15.dp))
        )
        Text(
            text = dataRumah.name.toString(),
            fontWeight = FontWeight.Medium,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}
