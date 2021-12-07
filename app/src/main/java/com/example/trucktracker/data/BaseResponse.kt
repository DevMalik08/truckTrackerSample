package com.example.trucktracker.data

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("responseCode") val response: Response,
    @SerializedName("data") val truckList: List<Truck>
)
