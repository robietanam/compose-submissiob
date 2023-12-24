package com.example.composesubmission.ui.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    rumahId: String,
    detailViewModel: DetailViewModel = viewModel(),
    navigateBack: () -> Unit,
    navigateToEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dataRumah = detailViewModel.dataRumah.collectAsState().value
    val loading = detailViewModel.loading.collectAsState().value
    if (dataRumah == null) {
        detailViewModel.getRumahId(rumahId = rumahId)
    }
    Scaffold(
        modifier.padding(horizontal = 12.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail")
                },
                actions = {
                    IconButton(onClick = { navigateToEdit(rumahId)}) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Localized description"
                        )
                    }

                },
                navigationIcon = {
                    IconButton(onClick = navigateBack ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (!loading && dataRumah != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    AsyncImage(
                        model = dataRumah.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                    Text(
                        text = dataRumah.name!!,
                        fontWeight = FontWeight.Medium,
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 16.dp)
                    )
                    Text(
                        text = dataRumah.description!!,
                        fontWeight = FontWeight.Normal,
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 20.dp)
                    )
                }


            } else {
                Box(
                    modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}
