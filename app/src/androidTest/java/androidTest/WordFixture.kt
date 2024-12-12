package androidTest

import com.lion.boo100word.ui.word.Word

object WordFixture {

    fun get(): List<Word> = listOf(
        Word(
            english = "apple",
            korean = "사과",
            isCorrect = true
        ),
        Word(
            english = "orange",
            korean = "오렌지",
            isCorrect = false
        ),
        Word(
            english = "peach",
            korean = "복숭아",
            isCorrect = true
        ),
        Word(
            english = "grape",
            korean = "포도",
            isCorrect = false
        ),
        Word(
            english = "watermelon",
            korean = "수박",
            isCorrect = false
        ),
        Word(
            english = "garlic",
            korean = "마늘",
            isCorrect = true
        ),
        Word(
            english = "anchovy",
            korean = "멸치",
            isCorrect = false
        ),
        Word(
            english = "onion",
            korean = "양파",
            isCorrect = true
        )
    )
}
