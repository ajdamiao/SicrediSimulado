package com.example.sicredisimulado.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sicredisimulado.data.repository.SicrediRepository
import com.example.sicredisimulado.exceptions.CustomException
import com.example.sicredisimulado.model.CheckInEvent
import com.example.sicredisimulado.model.Events
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Error

class HomeViewModel: ViewModel() {
    private val apiRepository = SicrediRepository().makeRequest()
    val eventsResponse: MutableLiveData<ArrayList<Events>> = MutableLiveData()
    val eventPostResponse: MutableLiveData<Any> = MutableLiveData()

    fun getEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val events = apiRepository.getEvents()

                eventsResponse.postValue(events)
            }catch (exception: Exception) {
                println(exception)
            }
        }
    }

    fun postEvent(checkInEvent: CheckInEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiRepository.checkInEvent(checkInEvent)

                if(response.code() != 200 || response.code() != 204) {
                    eventPostResponse.postValue(false)
                }
                else{
                    eventPostResponse.postValue(true)
                }
            }catch (exception: Exception) {
                when(exception) {
                    is HttpException -> {
                        val jsonParsed = JSONObject(exception.response()?.errorBody()!!.toString())
                        val gson = Gson()
                        val cException = gson.fromJson(jsonParsed.toString(), CustomException::class.java)

                        eventPostResponse.postValue(cException)
                    }
                }
            }
        }
    }
}