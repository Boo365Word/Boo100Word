package androidTest

import android.util.Log
import com.example.boo345word.data.repository.WordRepository
import com.example.boo345word.ui.detail.WordDetail
import com.example.boo345word.ui.word.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWordRepository : WordRepository {

    override suspend fun fetchWords(keyword: String): Flow<List<Word>> = flowOf(
        WordFixture.get()
    )

    override suspend fun fetchWordDetail(english: String): Flow<WordDetail> {
        Log.d("whoareyou", "fetchWordDetail: $english")
        return flowOf(
            WordDetailFixture.get()
        )
    }
}
