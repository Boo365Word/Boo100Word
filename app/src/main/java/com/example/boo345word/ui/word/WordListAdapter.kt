package com.example.boo345word.ui.word

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WordListAdapter(
    words: List<String>
) : RecyclerView.Adapter<WordListItemViewHolder>() {

    private var _words = words

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListItemViewHolder {
        return WordListItemViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: WordListItemViewHolder, position: Int) {
        holder.bind(_words[position])
    }

    override fun getItemCount(): Int = _words.size

    fun updateWords(words: List<String>){
        _words = words
        notifyDataSetChanged()
    }
}