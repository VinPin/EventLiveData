package com.vinpin.eventlivedata.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.vinpin.eventlivedata.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: EventViewModel
    private var normalObserverAttached = false
    private var stickyObserverAttached = false
    private var eventCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        binding.btnSendEvent.setOnClickListener {
            eventCount++
            val message = "事件 $eventCount: ${System.currentTimeMillis()}"
            viewModel.sendEvent(message)
        }

        binding.btnSendStickyEvent.setOnClickListener {
            eventCount++
            val message = "粘性事件 $eventCount: ${System.currentTimeMillis()}"
            viewModel.sendEvent(message)
        }

        binding.btnObserve.setOnClickListener {
            if (!normalObserverAttached) {
                normalObserverAttached = true
                viewModel.eventLiveData.observe(this) { message ->
                    binding.tvNormalObserver.append("$message\n")
                }
                binding.tvNormalObserver.append("普通观察者已注册\n")
            } else {
                binding.tvNormalObserver.append("普通观察者已存在\n")
            }
        }

        binding.btnObserveSticky.setOnClickListener {
            if (!stickyObserverAttached) {
                stickyObserverAttached = true
                viewModel.eventLiveData.observeSticky(this) { message ->
                    binding.tvStickyObserver.append("$message\n")
                }
                binding.tvStickyObserver.append("粘性观察者已注册\n")
            } else {
                binding.tvStickyObserver.append("粘性观察者已存在\n")
            }
        }
    }
}