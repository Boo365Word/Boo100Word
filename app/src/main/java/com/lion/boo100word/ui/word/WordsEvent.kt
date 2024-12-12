package com.lion.boo100word.ui.word

sealed interface WordsEvent {

    data object Error : WordsEvent
}
