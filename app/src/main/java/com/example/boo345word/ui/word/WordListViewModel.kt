package com.example.boo345word.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.repository.DefaultWordListRepository
import com.example.boo345word.data.repository.WordListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WordListViewModel(
    private val wordListRepository: WordListRepository = DefaultWordListRepository()
) : ViewModel() {

    private val _words: MutableStateFlow<Words> = MutableStateFlow(Words(listOf()))
    val words: StateFlow<Words>
        get() = _words

    init {
        fetchWords("")
    }

    fun fetchWords(keyword: String) {
        viewModelScope.launch {
            wordListRepository.fetchWords(keyword)
                .catch {
                    // todo: 예외 처리
                }.collect { value ->
                    _words.value = Words(value)
                }
        }
    }
}
