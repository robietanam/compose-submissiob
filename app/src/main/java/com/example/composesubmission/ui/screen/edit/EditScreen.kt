package com.example.composesubmission.ui.screen.edit

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.androidintermedieatesubmission.data.response.Rumah
import com.example.composesubmission.R
import com.example.composesubmission.data.helper.getImageUri
import com.example.composesubmission.data.helper.uriToFile
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    rumahId: String? = null,
    navigateBack: (Boolean) -> Unit,
    navigateSuccess: () -> Unit,
    editViewModel: EditViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val cameraUri = getImageUri(context)

    val isEdit = !rumahId.isNullOrEmpty()
    val dataRumah = editViewModel.dataRumah.value
    val statusUpload = editViewModel.statusRumah.value
    val loading = editViewModel.loading.value

    if (isEdit && dataRumah == null) {
        editViewModel.getRumahId(rumahId!!)
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var runOnce by remember { mutableStateOf(false)}


    var showDialog by remember { mutableStateOf(false) }

    if (dataRumah != null) {
        title = dataRumah.name!!
        description = dataRumah.description!!
        author = dataRumah.author!!
        imageUri = dataRumah.photoUrl?.toUri()
    }

    if (statusUpload != null && statusUpload.isStart && !runOnce) {
        if (statusUpload.error) {
            Toast.makeText(context, statusUpload.message, Toast.LENGTH_SHORT).show()
        } else {
            runOnce = true
            Toast.makeText(context, statusUpload.message, Toast.LENGTH_SHORT).show()
            navigateSuccess()

        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            imageUri = cameraUri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(cameraUri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog)
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = {

                            showDialog = false
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(cameraUri)
                            } else {
                                // Request a permission
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }) {
                            Text("Camera")
                        }

                        TextButton(onClick = {

                            showDialog = false
                            launcher.launch("image/*")
                        }) {
                            Text("Gallery")
                        }
                    }
                }
            }
        }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit" else "Tambah") },
                actions = {
                    if (dataRumah != null){
                        IconButton(onClick = {
                            editViewModel.deleteRumah(dataRumah)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Localized description"
                            )
                        }
                    }

                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack(false) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
            )
        }
    )
    {
        if (loading) {
            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier.padding(it)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            showDialog = true
                        },
                ) {

                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Create,
                            contentDescription = stringResource(id = R.string.noImage),
                            modifier.size(30.dp)
                        )
                    }

                }


                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    maxLines = 1,
                    modifier = modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 1,
                    modifier = modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    maxLines = 1,
                    modifier = modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                )


                Button(
                    modifier = modifier.padding(20.dp),
                    onClick = {
                        if (!title.isNullOrEmpty() && !description.isNullOrEmpty() && !author.isNullOrEmpty() && !imageUri.toString().isNullOrEmpty() && imageUri != null) {

                            runOnce = false
                            var imageFile : File? = null

                            if (!imageUri.toString().startsWith("https")){
                                imageFile = uriToFile(imageUri!!, context)
                            }

                            if (isEdit) {

                                editViewModel.setRumah(
                                    Rumah(
                                        id = rumahId,
                                        name = title,
                                        description = description,
                                        photoUrl = dataRumah!!.photoUrl,
                                        author = author
                                    ), imageFile
                                )
                            } else {
                                editViewModel.tambahRumah(
                                    Rumah(
                                        name = title,
                                        description = description,
                                        author = author
                                    ), imageFile!!
                                )
                            }


                        } else {
                            Toast.makeText(
                                context,
                                "Lengkapi data terlebih dahulu",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                    Text(if (isEdit) "Edit Rumah" else "Tambah Rumah")
                }

            }
        }

    }

}

