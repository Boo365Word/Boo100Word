package com.lion.boo100word.ui.game.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object GameTimer {
    private val _currentTimer = MutableStateFlow(0)
    val currentTimer: StateFlow<Int> = _currentTimer

    private var timerJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun startTimer() {
        // 기존 타이머 중지
        stopTimer()
        Log.d("Timer", "Timer isCancelled status :${timerJob?.isCancelled}") // 디버깅용 로그
        // 새로운 타이머 시작
        timerJob =
            coroutineScope.launch {
                Log.d("Timer", "Timer isActive status :${timerJob?.isActive}") // 디버깅용 로그
                _currentTimer.value = 0
                while (_currentTimer.value <= 20) {
                    delay(1000L)
                    Log.d("타이머", "${_currentTimer.value}")
                    _currentTimer.value += 1
                }
                Log.d("Timer", "Timer isCompleted status :${timerJob?.isCompleted}") // 디버깅용 로그
            }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun restartTimer() {
        stopTimer()
        timerJob =
            coroutineScope.launch {
                // 기존 값부터 다시 시작
                while (_currentTimer.value <= 20) {
                    delay(1000L)
                    Log.d("재시작 타이머", "${_currentTimer.value}")
                    _currentTimer.value += 1
                }
            }
    }
}
