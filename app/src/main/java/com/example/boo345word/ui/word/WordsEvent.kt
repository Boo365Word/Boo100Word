package com.example.boo345word.ui.word

sealed interface WordsEvent {

    data object Error: WordsEvent
}
