package com.example.composesubmission.data.model


data class ResultUpload(val imageUrl: String, val message: String, val error: Boolean)

data class FirestoreUpload(val message: String, val error: Boolean, var isStart: Boolean = false)
