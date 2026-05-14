package com.vinpin.eventlivedata.sample

import androidx.lifecycle.ViewModel
import com.vinpin.eventlivedata.EventLiveData

class EventViewModel : ViewModel() {

    val eventLiveData: EventLiveData<String> = EventLiveData()

    fun sendEvent(message: String) {
        eventLiveData.setValue(message)
    }
}