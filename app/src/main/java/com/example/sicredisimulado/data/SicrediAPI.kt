package com.example.sicredisimulado.data

import com.example.sicredisimulado.model.CheckInEvent
import com.example.sicredisimulado.model.Events
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SicrediAPI {
    @GET("api/events")
    suspend fun getEvents(): ArrayList<Events>

    @POST("api/checkin")
    suspend fun checkInEvent(@Body checkInEvent: CheckInEvent): Response<*>
}