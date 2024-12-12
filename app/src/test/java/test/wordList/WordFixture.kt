package test.wordList

import com.lion.boo100word.ui.word.Word

object WordFixture {

    fun get(): Word = Word(
        english = "no",
        korean = "nibh",
        isCorrect = false
    )

    fun getWords(): List<Word> = listOf(
        Word(
            english = "no",
            korean = "nibh",
            isCorrect = false
        ),
        Word(
            english = "no",
            korean = "nibh",
            isCorrect = false
        ),
        Word(
            english = "no",
            korean = "nibh",
            isCorrect = false
        )
    )
}
