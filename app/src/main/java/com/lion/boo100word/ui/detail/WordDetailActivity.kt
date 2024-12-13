package com.lion.boo100word.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lion.boo100word.R
import com.lion.boo100word.databinding.ActivityWordDetailBinding
import com.lion.boo100word.ui.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordDetailBinding
    private val viewModel: WordDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initWordDetailBinding()
        initWordDetailObserver()
    }

    private fun initWordDetailBinding() {
        binding = ActivityWordDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        /**
         * WordListActivity에서 intent로 전달한 Word(String)를 수신
         */
        intent.getStringExtra(KEY)?.also { word ->
            viewModel.fetchWordDetail(word)
            binding.tvWordEnglishDetailTitle.text = word
        } ?: finish()

        initWordDetailListener()
    }

    private fun initWordDetailListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initWordDetailObserver() {
        repeatOnStarted(this) {
            viewModel.wordDetail.collect { word ->
                if (word.english.isNotEmpty()) {
                    binding.rvExampleSentence.adapter = WordExampleSentenceAdapter(
                        word = word.english,
                        sentences = word.sentences
                    )

                    binding.tvWordEnglishDetail.text = word.english
                    binding.tvPronunciation.text = "[" + word.pronunciation + "]"
                    binding.tvWordKoreanDetail.text = word.korean
                    binding.tvExampleSentence.text = "예문"

                    word.symbol?.let { symbol ->
                        binding.ivWordSymbolImage.setImageResource(symbol)
                    } ?: binding.ivWordSymbolImage.setImageResource(0)
                }
            }
        }
        repeatOnStarted(this) {
            viewModel.event.collect { event ->
                when (event) {
                    is WordDetailEvent.Error -> {
                        Toast.makeText(
                            this@WordDetailActivity,
                            getString(R.string.word_detail_error_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY = "key"

        fun start(context: Context, word: String) {
            val intent = Intent(context, WordDetailActivity::class.java).apply {
                putExtra(KEY, word)
            }
            context.startActivity(intent)
        }
    }
}
