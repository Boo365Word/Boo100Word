package com.example.boo345word.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.repository.DefaultWordListRepository
import com.example.boo345word.data.repository.WordListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WordDetailViewModel(
    private val wordListRepository: WordListRepository = DefaultWordListRepository(),
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

    fun fetchWordDetail(word: String){
        viewModelScope.launch {
            wordListRepository.fetchWordDetail(word)
                .catch {
                    //todo: 예외 처리
                    Log.d("woogi", "fetchWordDetail: $it")
                }.collect{ detail ->
                    _wordDetail.value = detail
                }
        }
    }
}
