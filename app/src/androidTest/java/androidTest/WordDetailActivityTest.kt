// package androidTest
//
// import android.content.Intent
// import android.util.Log
// import androidx.activity.viewModels
// import androidx.lifecycle.ViewModelProvider
// import androidx.lifecycle.viewmodel.initializer
// import androidx.lifecycle.viewmodel.viewModelFactory
// import androidx.test.core.app.ActivityScenario
// import androidx.test.core.app.ApplicationProvider
// import androidx.test.espresso.Espresso.onView
// import androidx.test.espresso.assertion.ViewAssertions.matches
// import androidx.test.espresso.contrib.RecyclerViewActions
// import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
// import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
// import androidx.test.espresso.matcher.ViewMatchers.withId
// import androidx.test.espresso.matcher.ViewMatchers.withText
// import androidx.test.ext.junit.rules.ActivityScenarioRule
// import com.example.boo345word.R
// import com.example.boo345word.data.repository.WordRepository
// import com.example.boo345word.ui.detail.WordDetail
// import com.example.boo345word.ui.detail.WordDetailActivity
// import com.example.boo345word.ui.detail.WordDetailViewModel
// import com.example.boo345word.ui.detail.WordExampleSentenceViewHolder
// import org.hamcrest.CoreMatchers.allOf
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import retrofit2.http.GET
// import kotlin.reflect.KProperty
//
// import kotlin.reflect.full.declaredMemberProperties
// import kotlin.reflect.jvm.isAccessible
//
// class WordDetailActivityTest {
//
//    private val givenWordsDetail = WordDetailFixture.get()
//    private val intent = Intent(
//        ApplicationProvider.getApplicationContext(),
//        WordDetailActivity::class.java
//    ).apply {
//        putExtra(
//            "key",
//            givenWordsDetail.english
//        )
//    }
//
//    private lateinit var activityRule: ActivityScenario<WordDetailActivity>
//    private lateinit var wordRepository: WordRepository
//
//    @Before
//    fun setup() {
//        activityRule = ActivityScenario.launch(intent)
//        activityRule.onActivity { activity ->
//            val viewModelFactory = viewModelFactory {
//                initializer {
//                    WordDetailViewModel(FakeWordRepository())
//                }
//            }
//            val viewModel =
//                ViewModelProvider(activity, viewModelFactory)[WordDetailViewModel::class.java]
//            WordDetailActivity::class
//                .declaredMemberProperties
//                .filterIsInstance< KProperty<*>
//            }?.apply{
//                isAccessible = true
//                set(viewModel, "viewModel")
//            }
//
//            {
//                Log.d("woogi", "setup: $it")
//            }
//            val viewModelField = WordDetailActivity::class.java.getField("viewModel")
//
//            viewModelField.apply {
//                viewModelField.isAccessible = true
//                viewModelField.set(activity, viewModel)
//            }
//        }
//    }
//
//    @Test
//    fun 선택한_단어의_영어와_한글이_화면에_표시된다() {
//        onView(withId(R.id.tv_word_english_detail))
//            .check(matches(withText(givenWordsDetail.english)))
//
//        onView(withId(R.id.tv_word_korean_detail))
//            .check(matches(withText(givenWordsDetail.korean)))
//    }
//
//
//    @Test
//    fun 선택한_단어의_예문_리스트가_화면에_표시된다() {
//        activityRule = ActivityScenario.launch(intent)
//
//        givenWordsDetail.sentences.forEachIndexed { index, sentence ->
//            onView(withId(R.id.rv_example_sentence)).perform(
//                RecyclerViewActions.scrollToPosition<WordExampleSentenceViewHolder>(index)
//            ).check(
//                matches(
//                    hasDescendant(
//                        allOf(withText(sentence.korean), isDisplayed())
//                    )
//                )
//            ).check(
//                matches(
//                    hasDescendant(
//                        allOf(withText(sentence.english), isDisplayed())
//                    )
//                )
//            )
//        }
//    }
// }
