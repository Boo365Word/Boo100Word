package com.example.boo345word.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WordState(
    val word: String,
    val state: Boolean
)

@HiltViewModel
class GameViewModel
@Inject
constructor(
    private val repository: WordRepository
) : ViewModel() {

    private val _basicWordList = MutableStateFlow<List<BasicWord>>(emptyList())
    val basicWordList: StateFlow<List<BasicWord>> = _basicWordList

    private val _correctWordList = MutableStateFlow<MutableList<BasicWord>>(mutableListOf())
    val correctWordList: StateFlow<MutableList<BasicWord>> = _correctWordList

    private val _wrongWordList = MutableStateFlow<MutableList<BasicWord>>(mutableListOf())
    val wrongWordList: StateFlow<MutableList<BasicWord>> = _wrongWordList

    private val _currentState = MutableStateFlow<Int>(1)
    val currentState: StateFlow<Int> = _currentState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _basicWordList.value = repository.getFiveBasicWords().first()
            _basicWordList.value!!.forEach {
                repository.markWordAsLearned(it.word)
            }
        }
    }

    fun clearWordList() {
        _basicWordList.value = emptyList()
        _correctWordList.value = mutableListOf()
        _wrongWordList.value = mutableListOf()
        gameResult.clear()
    }

    fun updateState(state: Int) {
        _currentState.value = state
    }

    private val gameResult: ArrayList<WordState> = arrayListOf()

    fun saveCorrectWord(result: BasicWord) {
        _correctWordList.value.add(result)
        viewModelScope.launch(Dispatchers.IO) {
            repository.markWordAsLearned(result.word)
        }
    }

    fun saveWrongWord(result: BasicWord) {
        _wrongWordList.value.add(result)
    }
}
