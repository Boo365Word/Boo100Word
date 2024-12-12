package com.lion.boo100word.ui.word

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

/**
 * AsyncListDiffer를 내부적으로 사용하고 있는 ListAdapter를 적용
 */
class WordListAdapter(
    private val onWordClick: (word: String) -> Unit
) : ListAdapter<Word, WordListItemViewHolder>(WordDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListItemViewHolder {
        return WordListItemViewHolder.of(
            parent = parent,
            onClick = onWordClick
        )
    }

    override fun onBindViewHolder(holder: WordListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateWords(words: List<Word>) {
        submitList(words.toMutableList())
    }
}
