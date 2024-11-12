package androidTest.wordList

import com.example.boo345word.ui.word.Words

object WordsFixture {

    fun get(): Words = Words(
        value = WordFixture.getWords()
    )
}
