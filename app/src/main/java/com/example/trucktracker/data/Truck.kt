package com.example.trucktracker.data

import com.google.gson.annotations.SerializedName

data class Truck(
    @SerializedName("id") val id: Int,
    @SerializedName("truckNumber") val truckNumber: String,
    @SerializedName("lastWaypoint") val positionInfo: PositionInfo,
    @SerializedName("lastRunningState") val runningInfo: RunningInfo
)
