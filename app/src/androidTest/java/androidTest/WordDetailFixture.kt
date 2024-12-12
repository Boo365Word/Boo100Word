package androidTest

import com.lion.boo100word.ui.detail.Sentence
import com.lion.boo100word.ui.detail.WordDetail

object WordDetailFixture {

    fun get(): WordDetail = WordDetail(
        english = "eripuit",
        pronunciation = "scripta",
        korean = "luctus",
        sentences = listOf(
            Sentence(english = "similique", korean = "dicta"),
            Sentence(english = "fasfa", korean = "ad"),
            Sentence(english = "qweqwe", korean = "sad"),
            Sentence(english = "123123", korean = "qwer"),
            Sentence(english = "fokdpsakp", korean = "fge")
        )
    )
}
