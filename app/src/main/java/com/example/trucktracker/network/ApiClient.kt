package com.example.trucktracker.network

class ApiClient(private val apiService: ApiInterface) {

    suspend fun getTrucksList() = apiService.getTrucksList()
}