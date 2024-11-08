package com.example.boo345word.ui.detail

sealed interface WordDetailEvent {

    data object Error : WordDetailEvent
}
