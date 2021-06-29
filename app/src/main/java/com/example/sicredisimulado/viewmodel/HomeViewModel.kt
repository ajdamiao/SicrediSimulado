package com.example.sicredisimulado.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sicredisimulado.data.repository.SicrediRepository
import com.example.sicredisimulado.model.CheckInEvent
import com.example.sicredisimulado.model.Events
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val apiRepository = SicrediRepository().makeRequest()
    val eventsResponse: MutableLiveData<ArrayList<Events>> = MutableLiveData()

    fun getEvents()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val events = apiRepository.getEvents()

                eventsResponse.postValue(events)
            }catch (exception: Exception) {
                println(exception)
            }
        }
    }

    fun postEvent(checkInEvent: CheckInEvent)
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiRepository.checkInEvent(checkInEvent)
            }catch (exception: Exception) {
                println(exception)
            }
        }
    }

}