package test.wordList

import com.lion.boo100word.ui.word.Words

object WordsFixture {

    fun get(): Words = Words(
        value = WordFixture.getWords()
    )
}
