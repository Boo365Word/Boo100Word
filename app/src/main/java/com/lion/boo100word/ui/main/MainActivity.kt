package com.lion.boo100word.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lion.boo100word.R
import com.lion.boo100word.databinding.ActivityMainBinding
import com.lion.boo100word.ui.custom.InfoDialog
import com.lion.boo100word.ui.game.GameActivity
import com.lion.boo100word.ui.util.repeatOnStarted
import com.lion.boo100word.ui.word.WordListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMainBinding()
        initMainObserver()
        initMainClickListener()
    }

    private fun initMainBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater).also { binding ->
            setContentView(binding.root)
        }
    }

    private fun initMainObserver() {
        repeatOnStarted(this) {
            viewModel.progressRate.collect { progress ->
                when (progress) {
                    is Progress.ProgressRate -> {
                        binding.customHeartProgressBar.updateProgress(progress.rate)
                        getString(R.string.main_current_progress).format(
                            progress.total,
                            progress.right
                        ).also { description ->
                            setProgressDescriptionColor(description)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setProgressDescriptionColor(progressDescription: String) {
        SpannableStringBuilder(progressDescription).also {
            it.setSpan(
                ForegroundColorSpan(binding.root.context.getColor(R.color.highlighting_word)),
                0,
                3,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            it.setSpan(
                ForegroundColorSpan(binding.root.context.getColor(R.color.highlighting_word)),
                progressDescription.indexOf('중') + 1,
                progressDescription.indexOfLast { c ->
                    c == '개'
                },
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            binding.tvCurrentProgress.text = it
        }
    }

    private fun initMainClickListener() {
        with(binding) {
            btnGameStart.setOnClickListener {
                GameActivity.start(this@MainActivity)
            }
            btnWord.setOnClickListener {
                WordListActivity.start(this@MainActivity)
            }
            btnInfo.setOnClickListener {
                infoOverlay.also { overlay ->
                    overlay.visibility = View.VISIBLE

                    InfoDialog(
                        context = this@MainActivity,
                        overlay = overlay
                    )
                }
            }
        }
    }

    companion object {

        private const val INITIAL_PROGRESS_VALUE = 0.8f

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)

            context.startActivity(intent)
        }
    }
}
