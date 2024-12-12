package com.lion.boo100word.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.boo100word.data.repository.WordRepository
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
class WordDetailViewModel @Inject constructor(
    private val wordRepository: WordRepository
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
            wordRepository.getWordDetailByKeyword(word)
                .catch {
                    _event.emit(WordDetailEvent.Error)
                }.collect { detail ->
                    _wordDetail.value = detail
                }
        }
    }
}
