package androidTest

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.boo345word.R
import com.example.boo345word.ui.word.WordListActivity
import com.example.boo345word.ui.word.WordListAdapter
import com.example.boo345word.ui.word.WordListItemViewHolder
import com.example.boo345word.ui.word.Words
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WordListActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(WordListActivity::class.java)
    private val givenWords = WordFixture.get()

    @Before
    fun setUp() {
        activityRule.scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_word_list)
            val adapter = recyclerView.adapter as WordListAdapter

            adapter.updateWords(givenWords)
        }
    }

    @Test
    fun 어댑터에_입력으로_주어지는_단어들과_화면에_보여지는_단어들은_동일하다() {
        givenWords.forEachIndexed { position, word ->
            onView(withId(R.id.rv_word_list))
                .perform(
                    RecyclerViewActions.scrollToPosition<WordListItemViewHolder>(position)
                ).check(
                    matches(
                        hasDescendant(
                            allOf(withText(word.english), isDisplayed())
                        )
                    )
                )
        }
    }

    @Test
    fun 맞힌_단어_체크를_해제하면_틀린_단어만_화면에_표시된다() {
        // 맞힌 단어 체크 박스 해제
        onView(withId(R.id.cb_right_words))
            .perform(click())

        val words = Words(givenWords)

        // 맞힌 단어가 보이지 않는지
        words.gotARight.forEachIndexed { position, word ->
            onView(withText(word.korean)).check(doesNotExist())
        }

        // 틀린 단어가 보이는지
        words.gotAWrong.forEachIndexed { position, word ->
            onView(withId(R.id.rv_word_list)).perform(
                RecyclerViewActions.scrollToPosition<WordListItemViewHolder>(position)
            ).check(
                matches(
                    hasDescendant(
                        allOf(withText(word.english), isDisplayed())
                    )
                )
            ).check(
                matches(
                    hasDescendant(
                        allOf(withText(word.korean), isDisplayed())
                    )
                )
            )
        }
    }

    @Test
    fun 틀린_단어_체크를_해제하면_맞힌_단어만_화면에_표시된다() {
        // 틀린 단어 체크 박스 해제
        onView(withId(R.id.cb_wrong_words))
            .perform(click())

        val words = Words(givenWords)

        // 틀린 단어가 보이지 않는지
        words.gotAWrong.forEachIndexed { position, word ->
            onView(withText(word.korean)).check(doesNotExist())
        }

        // 맞힌 단어가 보이는지
        words.gotARight.forEachIndexed { position, word ->
            onView(withId(R.id.rv_word_list)).perform(
                RecyclerViewActions.scrollToPosition<WordListItemViewHolder>(position)
            ).check(
                matches(
                    hasDescendant(
                        allOf(withText(word.english), isDisplayed())
                    )
                )
            ).check(
                matches(
                    hasDescendant(
                        allOf(withText(word.korean), isDisplayed())
                    )
                )
            )
        }
    }

    @Test
    fun 모든_체크를_해제하면_아무_단어도_화면에_표시되지_않는다() {
        // 틀린 단어 체크 박스 해제
        onView(withId(R.id.cb_wrong_words))
            .perform(click())
        onView(withId(R.id.cb_right_words))
            .perform(click())

        // 모든 단어가 보이지 않는지
        givenWords.forEachIndexed { position, word ->
            onView(withText(word.korean)).check(doesNotExist())
        }
    }
}
