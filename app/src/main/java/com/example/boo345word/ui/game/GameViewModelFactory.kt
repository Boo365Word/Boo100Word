package com.example.boo345word.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.boo345word.data.repository.WordRepository

class GameViewModelFactory(
    private val repository: WordRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
