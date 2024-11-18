package com.example.boo345word.ui.detail

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boo345word.R
import com.example.boo345word.databinding.ItemExampleSentenceBinding

class WordExampleSentenceViewHolder private constructor(
    private val binding: ItemExampleSentenceBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(sentence: Sentence, targetWord: String) {
        binding.tvSentenceKorean.text = sentence.korean
        val from = sentence.english.indexOf(targetWord.replace('_', ' '))
        val to = from + targetWord.length

        /**
         * 검색한 단어의 색깔은 예문에서 다르게 표현하기 위한 코드
         */
        SpannableStringBuilder(sentence.english).also {
            it.setSpan(
                ForegroundColorSpan(binding.root.context.getColor(R.color.highlighting_word)),
                from,
                to,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            binding.tvSentenceEnglish.text = it
        }
    }

    companion object {

        fun of(parent: ViewGroup): WordExampleSentenceViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemExampleSentenceBinding.inflate(layoutInflater, parent, false)

            return WordExampleSentenceViewHolder(binding)
        }
    }
}
