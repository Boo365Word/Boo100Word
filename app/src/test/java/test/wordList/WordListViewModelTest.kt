package test.wordList

import com.lion.boo100word.data.repository.WordRepository
import com.lion.boo100word.ui.word.WordListViewModel
import com.lion.boo100word.ui.word.Words
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WordListViewModelTest {

    private lateinit var wordListViewModel: WordListViewModel
    private lateinit var wordRepository: WordRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        wordRepository = mockk()
        wordListViewModel = WordListViewModel(wordRepository)
    }

    @Test
    fun 단어_목록을_불러오면_해당_결과를_유지한다() {
        // given
        coEvery {
            wordRepository.getAllWords()
        } returns flowOf(WordFixture.getWords())

        // when
        wordListViewModel.getAllWords()

        val actual = wordListViewModel.words.value

        // then
        val expected = Words(WordFixture.getWords())

        assertEquals(expected, actual)
    }
}
