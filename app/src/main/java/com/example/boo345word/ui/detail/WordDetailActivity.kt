package com.example.boo345word.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.boo345word.R
import com.example.boo345word.databinding.ActivityWordDetailBinding
import com.example.boo345word.ui.util.repeatOnStarted

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
         * WordListActivity에서 intent로 전달한 Word를 수신
         */
        intent.getStringExtra(KEY)?.also { word ->
            viewModel.fetchWordDetail(word)
            binding.tvWord.text = word
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
            viewModel.wordDetail.collect {
                if (it.english.isNotEmpty()) {
                    binding.rvExampleSentence.adapter = WordExampleSentenceAdapter(
                        word = it.english,
                        sentences = it.sentences
                    )
                }
            }
        }
        repeatOnStarted(this) {
            handleWordDetailEvent()
        }
    }

    private suspend fun handleWordDetailEvent() {
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
