package com.example.composesubmission.ui.screen.edit

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidintermedieatesubmission.data.response.Rumah
import com.example.composesubmission.data.model.FirestoreUpload
import com.example.composesubmission.data.model.ResultUpload
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class EditViewModel : ViewModel() {

    private val _dataRumah = mutableStateOf<Rumah?>(null)
    val dataRumah: State<Rumah?> get()= _dataRumah

    private val _statusRumah = mutableStateOf<FirestoreUpload?>(null)
    val statusRumah: State<FirestoreUpload?> get() = _statusRumah

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error


    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading


    fun getRumahId(rumahId: String) {

        val firestore = Firebase.firestore

        _loading.value = true
        firestore.collection("rumah")
            .whereEqualTo("id", rumahId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.toObject(Rumah::class.java).apply {
                        id = document.id
                    }
                    _dataRumah.value = data
                }
                _loading.value = false
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Terdapat Error"
                _loading.value = false
            }

    }

    fun setRumah(dataRumah: Rumah, imageFile: File?) {
        val firestore = Firebase.firestore
        viewModelScope.launch {
            var dataRumahBaru = dataRumah


            if (imageFile != null){
                if (dataRumah.photoUrl?.startsWith("https://firebasestorage.googleapis.com") == true){
                    deleteGambar(dataRumah.photoUrl!!)
                }
                val imageUrl = viewModelScope.async{ uploadGambar(imageFile) }.await()
                if (imageUrl.error) {
                    _statusRumah.value = FirestoreUpload(imageUrl.message, imageUrl.error, isStart = true)
                    return@launch
                }

                dataRumahBaru = dataRumahBaru.copy( photoUrl = imageUrl.imageUrl)
                Log.d("ANJINGURL", imageUrl.toString())
            }



            var message = ""
            var error = false

            _loading.value = true
            firestore.collection("rumah")
                .document(dataRumah.id!!)
                .set(dataRumahBaru)
                .addOnSuccessListener {
                    _loading.value = false
                    message = "Sukses mengubah data"

                    _statusRumah.value = FirestoreUpload(message, error,  isStart = true)
                }.addOnFailureListener {
                    _loading.value = false
                    message = "Error mengubah data"
                    error = true


                    _statusRumah.value = FirestoreUpload(message, error,  isStart = true)
                }
        }



    }

    fun tambahRumah(dataRumah: Rumah, imageFile: File) {
        viewModelScope.launch {
            val firestore = Firebase.firestore

            val imageUrl = uploadGambar(imageFile)
            var message = ""
            var error = false

            _loading.value = true
            val doc = firestore.collection("rumah")
                .document()

            doc.set(dataRumah.copy(photoUrl = imageUrl.imageUrl, id = doc.id))
                .addOnSuccessListener {
                    _loading.value = false
                    message = "Sukses membuat data"

                    _statusRumah.value = FirestoreUpload(message, error,  isStart = true)
                }.addOnFailureListener {
                    _loading.value = false
                    message = "Error membuat data"
                    error = true

                    _statusRumah.value = FirestoreUpload(message, error,  isStart = true)
                }

        }
    }

    fun deleteRumah(dataRumah: Rumah){
        val firestore = Firebase.firestore
        viewModelScope.launch {
            if (dataRumah.photoUrl?.startsWith("https://firebasestorage.googleapis.com") == true) {
                deleteGambar(dataRumah.photoUrl!!)
            }

            var message = ""
            var error = false

            _loading.value = true
            firestore.collection("rumah")
                .document(dataRumah.id!!)
                .delete()
                .addOnSuccessListener {

                    _loading.value = false
                    message = "Sukses menghapus data"


                    _statusRumah.value = FirestoreUpload(message, error,  isStart = true)
                }.addOnFailureListener {

                    _loading.value = false
                    message = "Error menghapus data"
                    error = true

                    _statusRumah.value = FirestoreUpload(message, error,  isStart = true)
                }

        }

    }

    suspend fun deleteGambar(urlImage: String) {
        val storageRef = Firebase.storage.getReferenceFromUrl(urlImage)
        Log.d("MANABRO", storageRef.name)
        storageRef.delete().await()
    }


    // ASYNC NYA ANJING
    suspend fun uploadGambar(imageFile: File): ResultUpload {
        _loading.value = true

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageRef = Firebase.storage.reference
        val riversRef = storageRef.child("images/${imageFile.name.trim()}_${timeStamp}")
        val uploadTask =  riversRef.putFile(Uri.fromFile(imageFile)).await()
        var imageUrl = ""
        var message = ""
        var error = false

        if (uploadTask.task.isSuccessful || uploadTask.task.isComplete ) {
            imageUrl = riversRef.downloadUrl.await().toString()
            message = "Sukses dalam mengupload gambar"

            return ResultUpload(imageUrl, message, error)
        } else {

            error = true
            message = uploadTask.task.exception?.message ?: "Error dalam mengupload gambar"

            return ResultUpload(imageUrl, message, error)
        }

    }


}