package com.example.trucktracker.model

import com.example.trucktracker.network.ApiClient
import com.example.trucktracker.network.RetrofitBuilder

class TruckRepository {

    private var apiClient: ApiClient = ApiClient(RetrofitBuilder.apiService)

    suspend fun getTruckList() = apiClient.getTrucksList()
}