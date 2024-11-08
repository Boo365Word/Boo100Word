package com.example.boo345word.ui.detail

data class WordDetail(
    val english: String,
    val pronunciation: String,
    val korean: String,
    val sentences: List<Sentence>
)
