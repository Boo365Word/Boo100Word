package com.example.boo345word.ui.main

sealed interface Progress {

    data object Empty : Progress

    data class ProgressRate(
        val total: Int,
        val right: Int
    ) : Progress {

        val rate: Float = right.toFloat() / total
    }
}
