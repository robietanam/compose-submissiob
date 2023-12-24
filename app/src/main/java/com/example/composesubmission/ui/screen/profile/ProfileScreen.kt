package com.example.composesubmission.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composesubmission.R
import com.example.composesubmission.ui.theme.ComposeSubmissionTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.TopCenter
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(25.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.photo),
                    contentScale =  ContentScale.Crop,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape),

                )
                Text(
                    text = "Muhammad Robitul Anam",
                    fontWeight = FontWeight.Medium,
                    modifier = modifier
                        .padding(vertical = 20.dp)
                )
                Text(
                    text = "robietanam@gmail.com",
                    fontWeight = FontWeight.Medium,
                    modifier = modifier
                        .padding(vertical = 20.dp)
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JetHeroesAppPreview() {
    ComposeSubmissionTheme {
        ProfileScreen()
    }
}