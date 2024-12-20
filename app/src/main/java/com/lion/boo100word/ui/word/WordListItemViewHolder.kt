package com.lion.boo100word.ui.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lion.boo100word.databinding.ItemWordListBinding

class WordListItemViewHolder(
    private val binding: ItemWordListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(word: Word) {
        binding.tvWordEnglish.text = word.english
        binding.tvWordKorean.text = word.korean
        word.symbol?.let {
            binding.ivWordSymbolImage.setImageResource(it)
        } ?: binding.ivWordSymbolImage.setImageResource(0)
    }

    companion object {

        fun of(
            parent: ViewGroup,
            onClick: (word: String) -> Unit
        ): WordListItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemWordListBinding.inflate(layoutInflater, parent, false).apply {
                root.setOnClickListener {
                    onClick(tvWordEnglish.text.toString())
                }
            }

            return WordListItemViewHolder(binding)
        }
    }
}
