package com.example.boo345word.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boo345word.data.model.WordInfo

data class WordState(
    val word: String,
    val state: Boolean,
)

class GameViewModel : ViewModel() {
    private val _correctWordList = MutableLiveData<MutableList<WordInfo>>(mutableListOf())
    val correctWordList: LiveData<MutableList<WordInfo>> = _correctWordList

    private val _wrongWordList = MutableLiveData<MutableList<WordInfo>>(mutableListOf())
    val wrongWordList: LiveData<MutableList<WordInfo>> = _wrongWordList

    private val _wordList = MutableLiveData<List<WordInfo>>(emptyList())
    val wordList: LiveData<List<WordInfo>> = _wordList
    private val _currentState: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val currentState get() = _currentState

    fun setWordList(list: List<WordInfo>) {
        _wordList.value = list
    }

    fun getWordList(): List<WordInfo>? {
        val words = _wordList.value?.shuffled()
        _wordList.value = words?.take(5)
        return _wordList.value
    }

    fun clear() {
        gameResult.clear()
    }

    fun updateState(state: Int) {
        _currentState.value = state
    }

    val gameResult: ArrayList<WordState> = arrayListOf()

    fun saveCorrectWord(result: WordInfo) {
        _correctWordList.value?.add(result)
    }

    fun saveWrongWord(result: WordInfo) {
        _wrongWordList.value?.add(result)
    }
}
