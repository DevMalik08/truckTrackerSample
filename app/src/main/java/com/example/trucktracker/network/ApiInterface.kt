package com.example.trucktracker.network

import com.example.trucktracker.data.BaseResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET("searchTrucks?auth-company=PCH&companyId=33&deactivated=false&key=g2qb5jvucg7j8skpu5q7ria0mu&q-expand=true&q-include=lastRunningState,lastWaypoint")
    suspend fun getTrucksList(): BaseResponse
}