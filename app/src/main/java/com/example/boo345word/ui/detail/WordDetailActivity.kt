package com.example.boo345word.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.boo345word.databinding.ActivityWordDetailBinding
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wordDetail.collect {
                    if (it.english.isNotEmpty()) {
                        binding.rvExampleSentence.adapter = WordExampleSentenceAdapter(
                            word = it.english,
                            sentences = it.sentences
                        )
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
