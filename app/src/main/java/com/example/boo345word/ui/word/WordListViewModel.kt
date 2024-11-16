package com.example.boo345word.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.repository.DefaultWordRepository
import com.example.boo345word.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WordListViewModel(
    private val wordRepository: WordRepository = DefaultWordRepository(),
) : ViewModel() {

    private val _words: MutableStateFlow<Words> = MutableStateFlow(Words(listOf()))
    val words: StateFlow<Words>
        get() = _words.asStateFlow()

    private val _event: MutableSharedFlow<WordsEvent> = MutableSharedFlow()
    val event: SharedFlow<WordsEvent>
        get() = _event.asSharedFlow()

    init {
        fetchWords("")
    }

    fun fetchWords(keyword: String) {
        viewModelScope.launch {
            wordRepository.fetchWords(keyword)
                .catch {
                    _event.emit(WordsEvent.Error)
                }.collect { value ->
                    _words.value = Words(value)
                }
        }
    }
}
