package com.vmm408.voznickandroid.ui.main.nav1.timerandcoroutine

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_coroutine_timer.*
import kotlinx.coroutines.*

class CoroutineTimerFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_coroutine_timer
    override val TAG = "CoroutineTimerFragment"

    private var timeInMillis = 1000000L
        set(value) {
            field = value
            timerField?.text = timeInMillis.div(1000).toString()
        }
    private var job: Job = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timeInMillis = 1000000L

        startButton?.setOnClickListener {
            if (null == job) {
                viewLifecycleOwner.lifecycleScope.launch {
                    var i = startTim
                }
            }
        }
        pauseButton?.setOnClickListener {
            job?.cancel()
            job = null
        }
        stopButton?.setOnClickListener {
            job?.cancel()
            job = null
            timeInMillis = 1000000
        }
    }

    private suspend fun startTimer() {
        job = CoroutineScope(Dispatchers.Main).async {
            while (timeInMillis > 1000) {
                delay(1000)
                timeInMillis = timeInMillis.minus(1000)
            }
        }.await()
    }
}