package com.lion.boo100word.ui.word

import androidx.annotation.DrawableRes

data class Word(
    val english: String,
    val korean: String,
    val isCorrect: Boolean
) {

    @DrawableRes
    val symbol: Int? = WordSymbols.find(english)
}
