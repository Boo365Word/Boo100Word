package com.example.boo345word.ui.word

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.boo345word.databinding.ActivityWordListBinding
import com.example.boo345word.ui.detail.WordDetailActivity
import kotlinx.coroutines.launch

class WordListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordListBinding
    private val viewModel: WordListViewModel by viewModels()
    private val wordListAdapter = WordListAdapter(emptyList()){ word ->
        WordDetailActivity.start(this, word)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initWordListBinding()
        initWordListListener()
    }

    private fun initWordListBinding() {
        binding = ActivityWordListBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.rvWordList.adapter = wordListAdapter
        }
        initWordListObserver()
    }

    private fun initWordListObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.words.collect { words ->
                    wordListAdapter.updateWords(words)
                    if (words.isEmpty()) {
                        binding.ivNoMatchingWords.visibility = View.VISIBLE
                        binding.tvNoMatchingWords.visibility = View.VISIBLE
                    }else {
                        binding.ivNoMatchingWords.visibility = View.GONE
                        binding.tvNoMatchingWords.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initWordListListener() {
        binding.tfWordSearchingField.setOnSearchingTextFieldListener { keyword ->
            viewModel.fetchWords(keyword)
        }
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, WordListActivity::class.java)

            context.startActivity(intent)
        }
    }
}