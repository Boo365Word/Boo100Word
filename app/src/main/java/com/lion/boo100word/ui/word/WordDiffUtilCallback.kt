package com.lion.boo100word.ui.word

import androidx.recyclerview.widget.DiffUtil

class WordDiffUtilCallback : DiffUtil.ItemCallback<Word>() {

    /**
     * 원래는 oldItem과 newItem을 식별할 수 있는 id값을 가지고 많이 비교하는데, 현재는 그런 값이 없어서 다음처럼 작성했습니다.
     */
    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.english == newItem.english
    }

    /**
     * areItemsTheSame()의 반환값이 true인 경우 이 함수를 통해서 각 객체의 내용이 동일한지 비교
     */
    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.isCorrect == newItem.isCorrect &&
            oldItem.english == newItem.english &&
            oldItem.korean == newItem.korean &&
            oldItem.symbol == newItem.symbol
    }
}
