package com.example.boo345word.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

interface WordListRepository {

    suspend fun fetchWords(keyword: String): Flow<List<String>>
}

class DefaultWordListRepository : WordListRepository {

    private val words = listOf(
        "apple", "orange", "apple", "orange", "apple", "orange", "apple", "orange",
    )

    override suspend fun fetchWords(keyword: String): Flow<List<String>> = flow {
        if (keyword.isEmpty()) emit(words)

        emit(
            words.filter { word ->
                word.contains(keyword)
            }
        )
    }
}

class WordListViewModel(

) : ViewModel() {

    private val wordListRepository: WordListRepository = DefaultWordListRepository()

    private val _words: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val words: StateFlow<List<String>>
        get() = _words

    init {
        fetchWords("")
    }

    fun fetchWords(keyword: String) {
        viewModelScope.launch {
            wordListRepository.fetchWords(keyword)
                .catch {
                    //todo: 예외 처리
                }.collect { searched ->
                    _words.value = searched
                }
        }
    }
}