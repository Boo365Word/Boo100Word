package com.example.boo345word.ui.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boo345word.databinding.ItemWordListBinding

class WordListItemViewHolder(
    private val binding: ItemWordListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(word: String) {
        binding.tvWordDescription.text = word
    }

    companion object {

        fun of(
            parent: ViewGroup,
            onClick: (word: String) -> Unit
        ): WordListItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemWordListBinding.inflate(layoutInflater, parent, false).apply {
                root.setOnClickListener {
                    onClick(tvWordDescription.text.toString())
                }
            }

            return WordListItemViewHolder(binding)
        }
    }
}
