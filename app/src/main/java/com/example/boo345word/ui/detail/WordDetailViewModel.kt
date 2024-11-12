package com.example.boo345word.ui.detail

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

class WordDetailViewModel(
    private val wordRepository: WordRepository = DefaultWordRepository()
) : ViewModel() {

    private val _wordDetail: MutableStateFlow<WordDetail> = MutableStateFlow(
        WordDetail(
            english = "",
            pronunciation = "",
            korean = "",
            sentences = listOf()
        )
    )
    val wordDetail: StateFlow<WordDetail>
        get() = _wordDetail.asStateFlow()

    private val _event: MutableSharedFlow<WordDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<WordDetailEvent>
        get() = _event.asSharedFlow()

    fun fetchWordDetail(word: String) {
        viewModelScope.launch {
            wordRepository.fetchWordDetail(word)
                .catch {
                    _event.emit(WordDetailEvent.Error)
                }.collect { detail ->
                    _wordDetail.value = detail
                }
        }
    }
}
