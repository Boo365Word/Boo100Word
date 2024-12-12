package test.wordDetail

import app.cash.turbine.test
import com.lion.boo100word.data.repository.WordRepository
import com.lion.boo100word.ui.detail.WordDetail
import com.lion.boo100word.ui.detail.WordDetailEvent
import com.lion.boo100word.ui.detail.WordDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WordDetailViewModelTest {

    private lateinit var wordDetailViewModel: WordDetailViewModel
    private lateinit var wordRepository: WordRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        wordRepository = mockk()
        wordDetailViewModel = WordDetailViewModel(wordRepository)
    }

    @Test
    fun 단어의_예문_조회에_성공하면_해당_결과를_유지한다() {
        // given
        단어의_예문_조회_요청의_결과가_다음과_같을_때(WordDetailFixture.get())

        // when
        단어의_예문_조회_요청을_하면()

        val actual = wordDetailViewModel.wordDetail.value

        // then
        val expected = WordDetailFixture.get()

        assertEquals(expected, actual)
    }

    @Test
    fun 단어의_예문_조회에_실패하면_실패_이벤트가_발생한다() = runTest {
        // given
        단어의_예문_조회_요청이_실패할_때()

        wordDetailViewModel.event.test {
            // when
            단어의_예문_조회_요청을_하면()

            val actual = awaitItem()

            val expected = WordDetailEvent.Error

            assertEquals(expected, actual)
        }
    }

    fun 단어의_예문_조회_요청의_결과가_다음과_같을_때(detail: WordDetail) {
        coEvery {
            wordRepository.getWordDetailByKeyword(any())
        } returns flowOf(detail)
    }

    fun 단어의_예문_조회_요청을_하면() {
        wordDetailViewModel.fetchWordDetail("apple")
    }

    fun 단어의_예문_조회_요청이_실패할_때() {
        coEvery {
            wordDetailViewModel.fetchWordDetail(any())
        } throws Throwable()
    }
}
