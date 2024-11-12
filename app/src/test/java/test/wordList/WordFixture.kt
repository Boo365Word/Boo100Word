package test.wordList

import com.example.boo345word.ui.word.Word

object WordFixture {

    fun get(): Word = Word(
        english = "no",
        korean = "nibh",
        isCorrect = false,
        symbol = null
    )

    fun getWords(): List<Word> = listOf(
        Word(
            english = "no",
            korean = "nibh",
            isCorrect = false,
            symbol = null
        ),
        Word(
            english = "no",
            korean = "nibh",
            isCorrect = false,
            symbol = null
        ),
        Word(
            english = "no",
            korean = "nibh",
            isCorrect = false,
            symbol = null
        )
    )
}
