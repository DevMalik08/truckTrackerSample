package com.example.trucktracker.data

import com.google.gson.annotations.SerializedName

data class RunningInfo(
    @SerializedName("truckRunningState") val runningState: Int,
    @SerializedName("stopStartTime") val stopStartTime: Long
)
