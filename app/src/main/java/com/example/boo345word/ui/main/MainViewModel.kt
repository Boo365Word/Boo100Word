package com.example.boo345word.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boo345word.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {

    private val _progressRate: MutableStateFlow<Progress> = MutableStateFlow(Progress.Empty)
    val progressRate: StateFlow<Progress>
        get() = _progressRate.asStateFlow()

    init {
        getProgressRate()
    }

    private fun getProgressRate() {
        viewModelScope.launch {
            wordRepository.getAllWords().collect { words ->
                _progressRate.value = Progress.ProgressRate(
                    total = words.size,
                    right = words.count { it.isCorrect }
                )
            }
        }
    }
}
