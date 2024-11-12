package com.example.boo345word.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.repository.DefaultWordRepository
import com.example.boo345word.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WordListViewModel(
    private val wordRepository: WordRepository = DefaultWordRepository()
) : ViewModel() {

    private val _words: MutableStateFlow<Words> = MutableStateFlow(Words(listOf()))
    val words: StateFlow<Words>
        get() = _words

    init {
        fetchWords("")
    }

    fun fetchWords(keyword: String) {
        viewModelScope.launch {
            wordRepository.fetchWords(keyword)
                .collect { value ->
                    _words.value = Words(value)
                }
        }
    }
}
