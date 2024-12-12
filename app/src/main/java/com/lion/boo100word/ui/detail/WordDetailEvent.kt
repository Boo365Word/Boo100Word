package com.lion.boo100word.ui.detail

sealed interface WordDetailEvent {

    data object Error : WordDetailEvent
}
