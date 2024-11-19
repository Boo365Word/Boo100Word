package com.example.boo345word.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {

    private val _words: MutableStateFlow<Words> = MutableStateFlow(Words(listOf()))
    val words: StateFlow<Words>
        get() = _words.asStateFlow()

    private val _event: MutableSharedFlow<WordsEvent> = MutableSharedFlow()
    val event: SharedFlow<WordsEvent>
        get() = _event.asSharedFlow()

    init {
        getAllWords()
    }

    fun getAllWords() {
        viewModelScope.launch {
            wordRepository.getAllWords()
                .catch {
                    _event.emit(WordsEvent.Error)
                }.collect { value ->
                    _words.value = Words(value)
                }
        }
    }

    fun getWordsByKeyword(keyword: String) {
        if (keyword.isEmpty()) {
            getAllWords()
            return
        }
        viewModelScope.launch {
            wordRepository.getWordsByKeyword(keyword)
                .catch {
                    _event.emit(WordsEvent.Error)
                }.collect { value ->
                    _words.value = Words(value)
                }
        }
    }
}
