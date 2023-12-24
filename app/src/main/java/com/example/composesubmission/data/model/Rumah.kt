package com.example.androidintermedieatesubmission.data.response

import com.google.gson.annotations.SerializedName


data class Rumah (

    @SerializedName("id") var id: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("description") var description: String? = "",
    @SerializedName("author") var author: String? = "",
    @SerializedName("photoUrl") var photoUrl: String? = "",
)

