package com.vinpin.eventlivedata

import androidx.lifecycle.Observer

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/8/21 21:29
 * desc   : 一个可以防止数据倒灌的[Observer]实现类，搭配[EventLiveData]的使用。
 */
class EventObserver<T>(
        private val observer: Observer<in T>,
        private val version: Int,
        private val liveData: EventLiveData<T>
) : Observer<T> {

    override fun onChanged(t: T?) {
        if (liveData.getCurrentVersion() > version) {
            observer.onChanged(t)
        }
    }
}