package com.example.composesubmission.ui.screen.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.androidintermedieatesubmission.data.response.Rumah
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class DetailViewModel : ViewModel() {

    private val _dataRumah = MutableStateFlow<Rumah?>(null)
    val dataRumah: MutableStateFlow<Rumah?> = _dataRumah

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> get() = _loading

//    init {
//        viewModelScope.launch {
//            getRumahId()
//        }
//    }

    fun getRumahId(rumahId: String){

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
}