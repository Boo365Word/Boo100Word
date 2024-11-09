package com.example.boo345word.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.model.WordInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class WordState(
    val word: String,
    val state: Boolean,
)

class GameViewModel : ViewModel() {
    @Suppress("ktlint:standard:backing-property-naming")
//    private val _timerRemaining = MutableLiveData(20)
    private val _currentTimer = MutableStateFlow(0)
    val currentTimer: StateFlow<Int> = _currentTimer

    private val _wordList = MutableLiveData<List<WordInfo>>(emptyList())
    val wordList: LiveData<List<WordInfo>> = _wordList
    private val _currentState: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    private var timerJob: Job = Job()
    private var jobCounter = 0

    val currentState get() = _currentState

    fun setWordList(list: List<WordInfo>) {
        _wordList.value = list
    }

    fun getWordList(): List<WordInfo>? =
        _wordList.value
            ?.shuffled()
            ?.take(5)

    fun clear() {
        gameResult.clear()
    }

    fun updateState(state: Int) {
        _currentState.value = state
    }

    fun startTimer() {
        // 기존 타이머 중지
        stopTimer()
        Log.d("job 상태 : ", "${timerJob.isActive}")
        if (!timerJob.isActive) {
            // 새로운 타이머 시작
            timerJob =
                viewModelScope.launch {
                    for (time in 0..20) {
                        _currentTimer.value = time
                        delay(1000L)
                    }
                }
        }
    }

    fun restartTimer() {
        stopTimer()
        timerJob =
            viewModelScope.launch {
                // 기존 값부터 다시 시작
                for (time in _currentTimer.value..20) {
                    _currentTimer.value = time
                    delay(1000L)
                }
            }
    }

    fun stopTimer() {
        timerJob.cancel()
    }

    val gameResult: ArrayList<WordState> = arrayListOf()

    fun saveGameResult(result: WordState) {
        // 게임 결과를 반영해주기
        gameResult.add(result)
        println("게임 상태 : $gameResult")
    }
}
