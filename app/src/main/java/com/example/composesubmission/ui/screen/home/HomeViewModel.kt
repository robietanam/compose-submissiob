package com.example.composesubmission.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidintermedieatesubmission.data.response.Rumah
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiList = MutableStateFlow<List<Rumah>>(emptyList())
    val uiList: MutableStateFlow<List<Rumah>> = _uiList

    private val _query = mutableStateOf<String>("")
    val query: State<String> get() = _query

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> get() = _loading


    val isRefresh = mutableStateOf<Boolean>(false)

    init {
        viewModelScope.launch {
            getRumahCloud()
        }
    }

    fun getRumahCloud(){

        val firestore = Firebase.firestore
        val rumahData = mutableListOf<Rumah>()

        _loading.value = true
        firestore.collection("rumah")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.toObject(Rumah::class.java).apply {
                        id = document.id
                    }
                    rumahData.add(data)
                }

                _uiList.value = rumahData.sortedBy { it.name }

                _loading.value = false
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Terdapat Error"
                _loading.value = false
            }

    }

    fun searchHeroes(newQuery: String){
        _query.value = newQuery
        val firestore = Firebase.firestore

        val rumahData = mutableListOf<Rumah>()

        firestore.collection("rumah")
            .whereGreaterThanOrEqualTo("name", "${_query.value}")
            .whereLessThanOrEqualTo("name", "${_query.value}\uF7FF")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val data = document.toObject(Rumah::class.java).apply {
                        id = document.id
                    }
                    rumahData.add(data)
                }

                _uiList.value = rumahData.sortedBy { it.name }

                _loading.value = false
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Terdapat Error"
                _loading.value = false
            }
    }

}