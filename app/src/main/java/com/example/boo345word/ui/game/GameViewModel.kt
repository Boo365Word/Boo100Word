package com.example.boo345word.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val basicWordList: StateFlow<List<BasicWord>>
        get() = _basicWordList.asStateFlow()

    private val _correctWordList = MutableStateFlow<MutableList<BasicWord>>(mutableListOf())
    val correctWordList: StateFlow<MutableList<BasicWord>>
        get() = _correctWordList.asStateFlow()

    private val _wrongWordSet = MutableStateFlow<MutableSet<BasicWord>>(mutableSetOf())
    val wrongWordSet: StateFlow<Set<BasicWord>>
        get() = _wrongWordSet.asStateFlow()

    private val _currentState = MutableStateFlow<Int>(1)
    val currentState: StateFlow<Int>
        get() = _currentState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _basicWordList.value = repository.getFiveBasicWords().first()
        }
    }

    fun clearWordList() {
        _basicWordList.value = emptyList()
        _correctWordList.value = mutableListOf()
        _wrongWordSet.value = mutableSetOf()
        gameResult.clear()
    }

    fun updateState(state: Int) {
        _currentState.value = state
    }

    private val gameResult: ArrayList<WordState> = arrayListOf()

    fun saveCorrectWord(result: BasicWord) {
        _correctWordList.value.add(result)
        // db에 해당 내용 반영하기
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStatus(result.word)
        }
    }

    fun saveWrongWord(result: BasicWord) {
        _wrongWordSet.value.add(result)
    }
}
