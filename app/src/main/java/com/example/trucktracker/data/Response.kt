package com.example.trucktracker.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("responseCode") val code: Int,
    @SerializedName("message") val message: String
)
