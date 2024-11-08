package com.example.boo345word.ui.word

import androidx.recyclerview.widget.DiffUtil

class WordDiffUtilCallback : DiffUtil.ItemCallback<Word>() {

    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.isCorrect == newItem.isCorrect &&
            oldItem.english == newItem.english &&
            oldItem.korean == newItem.korean &&
            oldItem.symbol == newItem.symbol
    }
}
