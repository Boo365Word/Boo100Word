package com.lion.boo100word.ui.detail

import androidx.annotation.DrawableRes
import com.lion.boo100word.ui.word.WordSymbols

data class WordDetail(
    val english: String,
    val pronunciation: String,
    val korean: String,
    val sentences: List<Sentence>
) {

    @DrawableRes
    val symbol: Int? = WordSymbols.find(english)
}
