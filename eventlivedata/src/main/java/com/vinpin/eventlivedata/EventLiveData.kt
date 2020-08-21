package com.vinpin.eventlivedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/8/21 21:29
 * desc   : 一个可以防止数据倒灌的MutableLiveData子类。
 */
class EventLiveData<T> : MutableLiveData<T>() {

    private var mCurrentVersion: Int = -1

    override fun setValue(value: T) {
        mCurrentVersion++
        super.setValue(value)
    }

    override fun postValue(value: T) {
        mCurrentVersion++
        super.postValue(value)
    }

    /**
     * 将给定观察者添加到给定所有者的生命周期内的观察者列表中，非粘性事件。
     */
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, EventObserver(observer, mCurrentVersion, this))
    }

    /**
     * 将给定观察者添加到给定所有者的生命周期内的观察者列表中，粘性事件。
     */
    fun observeSticky(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, EventObserver(observer, -1, this))
    }

    /**
     * 将给定的观察者添加到观察者列表中，非粘性事件。
     *
     * @return 给定的观察者的EventObserver
     */
    fun observeForeverEvent(observer: Observer<in T>): Observer<T> {
        val eventObserver = EventObserver(observer, mCurrentVersion, this)
        observeForever(eventObserver)
        return eventObserver
    }

    /**
     * 将给定的观察者添加到观察者列表中，粘性事件。
     *
     * @return 给定的观察者的EventObserver
     */
    fun observeStickyForeverEvent(observer: Observer<in T>): Observer<T> {
        val eventObserver = EventObserver(observer, -1, this)
        observeForever(eventObserver)
        return eventObserver
    }

    fun getCurrentVersion(): Int = mCurrentVersion
}