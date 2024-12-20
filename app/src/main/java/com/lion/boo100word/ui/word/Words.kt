package com.lion.boo100word.ui.word

data class Words(val value: List<Word>) {

    val gotARight = value.filter { word ->
        word.isCorrect
    }
    val gotAWrong = value.filter { word ->
        !word.isCorrect
    }
}
