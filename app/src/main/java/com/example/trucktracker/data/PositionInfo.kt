package com.example.trucktracker.data

import com.google.gson.annotations.SerializedName

data class PositionInfo(
    @SerializedName("lat") val latitude: Float,
    @SerializedName("lng") val longitude: Float,
    @SerializedName("speed") val speed: Float,
    @SerializedName("createTime") val createTime: Long,
    @SerializedName("ignitionOn") val isIgnition: Boolean
)