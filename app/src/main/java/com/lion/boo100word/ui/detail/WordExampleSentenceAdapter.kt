package com.lion.boo100word.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WordExampleSentenceAdapter(
    private val word: String,
    private val sentences: List<Sentence>
) : RecyclerView.Adapter<WordExampleSentenceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordExampleSentenceViewHolder {
        return WordExampleSentenceViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: WordExampleSentenceViewHolder, position: Int) {
        holder.bind(
            sentence = sentences[position],
            targetWord = word
        )
    }

    override fun getItemCount(): Int {
        return sentences.size
    }
}
