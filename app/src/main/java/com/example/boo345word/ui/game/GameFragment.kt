package com.example.boo345word.ui.game

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.boo345word.R
import com.example.boo345word.data.model.WordInfo
import com.example.boo345word.databinding.FragmentGameBinding
import com.example.boo345word.ui.classifier.DrawClassifier
import com.example.boo345word.ui.custom.GameResultDialog
import com.example.boo345word.ui.custom.HintDialog
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

class GameFragment :
    Fragment(),
    GameResultDialog.GameResultDialogListener {
    private lateinit var wordList: List<WordInfo>
    private val viewmodel by viewModels<GameViewModel>()
    private var currentWord: WordInfo? = null
    private lateinit var binding: FragmentGameBinding
    private var stateCount: Int = 5
    private var currentState = 1
    private var falseCount = 0
    private var isPaused: Boolean = false
    private val gameTimer = GameTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGameBinding.inflate(inflater)
        requireContext().loadJsonFromAssets()
        viewmodel.getWordList()
        return binding.root
    }

    private fun getRandomWord() {
        // 첫 게임인 경우 무작위 5개 선택된 단어 리스트를 가져온다.
        if (currentState == 1) {
            lifecycleScope.launch {
                viewmodel.wordList.observe(viewLifecycleOwner, {
                    wordList = it
                })
            }
        }
        // 가져온 5개 단어 중 현재 게임 단계에 맞는 단어를 얻는다.
        currentWord = viewmodel.wordList.value?.get(currentState - 1)
        binding.txtWord.text = currentWord?.word
    }

    private fun startGame() {
        // 현재 게임 단계가 5보다 작을 경우 다음 게임을 시작한다.
        if (currentState <= stateCount) {
            getRandomWord()
            // 새 게임을 시작할 준비를 한다.
            // 타이머 시작
            gameTimer.startTimer()
            startNextGame()
        } else {
            showGameResult()
        }
    }

    private fun skip() {
        falseCount = 0
        saveGameResult(false)
        if (currentState < stateCount) {
            currentState++
            viewmodel.updateState(currentState)
            gameTimer.stopTimer()
            startGame()
        } else {
            // 만약 현재 단계 >= 5 라면 결과창 보이기
            showGameResult()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val classifier = DrawClassifier(requireActivity())
        if (savedInstanceState == null) {
            startGame()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            // 여기서 두번째 Job이 생성됨 -> 결과적으로 타이머가 동시에 2번 실행됨
            gameTimer.currentTimer.collect {
                binding.timeProgressBar.progress = it
                if (it == 20) {
                    // 시간이 다 된 경우 넘어가기
                    skip()
                }
            }
        }

        with(binding) {
            btnPause.setOnClickListener {
                isPaused = !isPaused
                if (isPaused) {
                    // 중지버튼 누른 경우 타이머 중지
                    gameTimer.stopTimer()
                    binding.btnPause.setImageResource(R.drawable.btn_play)
                } else {
                    // 다시 한번 누른 경우 타이머 재시작
                    gameTimer.restartTimer()
                    binding.btnPause.setImageResource(R.drawable.btn_pause)
                }
            }
            btnSkip.setOnClickListener {
                skip()
            }

            drawingView.setOnDrawingCompletedListener {
                // 그림을 그릴 때 1 ± 5번째 게임이라면
                if (currentState <= 5) {
                    // 게임을 시작한다.
                    val newBitmap = binding.drawingView.getBitmap()
                    val result = classifier.classify(newBitmap, currentWord?.word.toString())
                    // 예측 결과가 정답이라면
                    if (result) {
                        lifecycleScope.launch {
                            binding.ivGhost.setImageResource(R.drawable.img_ghost_v2)
                            binding.btnHint.visibility = View.GONE
                            binding.lottieAnimationView.visibility = View.VISIBLE
                            binding.txtModelSpeech.text = "\"${currentWord?.meaning}\" 정답~!"
                            saveGameResult(true)
                            delay(2000)
                            currentState++
                            viewmodel.updateState(currentState)
                            // 정답이라면 다음으로 넘어간다.
                            startGame()
                        }
                    } else {
                        // 예측 결과가 오답이라면
                        // 힌트를 확인해봐가 나오기까지 틀린 횟수
                        falseCount++
                        binding.ivGhost.setImageResource(R.drawable.ghost_v2)
                        if (falseCount == 3) {
                            binding.txtModelSpeech.text = "힌트를 확인해봐!"
                            binding.btnHint.visibility = View.VISIBLE
                        } else {
                            binding.txtModelSpeech.text = "다시 그려줄래?!"
                        }
                    }
                }
            }

            timeProgressBar.progress = currentState

            icEraser.setOnClickListener {
                binding.drawingView.clearCanvas()
            }

            editHint.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    if (binding.editHint.text.toString() == currentWord?.meaning) {
                        Toast.makeText(context, "맞았어요!! 힌트를 보고 다시 그려볼까요?", Toast.LENGTH_SHORT).show()
                        binding.btnHint.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(context, "다시 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }
                    true
                } else {
                    false
                }
            }

            btnHint.setOnClickListener {
                currentWord?.let {
                    val dialog = HintDialog(it)
                    dialog.show(parentFragmentManager, "HintDialog")
                }
            }
        }
    }

    // json 파일 읽기
    private fun Context.getWords(): List<WordInfo> {
        val inputStream = assets.open("class_names.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val wordList = Gson().fromJson(json, Array<WordInfo>::class.java).toList()
        return wordList
    }

    // assets에서 Json 파일 읽어오기
    private fun Context.loadJsonFromAssets() {
        try {
            val wordList = getWords()
            viewmodel.setWordList(wordList)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun init() {
        // 다시 시작
        currentState = 1
        wordList = emptyList()
        viewmodel.clear()
        startNextGame()
    }

    private fun startNextGame() {
        // 컨버스를 초기화하고, 초기 유령이미지를 보인다.
        binding.btnPause.setImageResource(R.drawable.btn_pause)
        binding.drawingView.clearCanvas()
        binding.btnHint.visibility = View.GONE
        binding.lottieAnimationView.visibility = View.GONE
        binding.ivGhost.setImageResource(R.drawable.img_ghost_v2)
        binding.txtModelSpeech.text = "음 ~ 뭐지"
        binding.txtCurrentState.text = String.format(Locale.ENGLISH, "%d", currentState)
    }

    private fun saveGameResult(result: Boolean) {
        lifecycleScope.launch {
            if (result) {
                viewmodel.saveCorrectWord(currentWord!!)
            } else {
                viewmodel.saveWrongWord(currentWord!!)
            }
        }
    }

    private fun showGameResult() {
        // 이 때 뷰모델 데이터 이용하기
        gameTimer.stopTimer()
        val dialog = GameResultDialog(viewmodel.correctWordList.value!!.toList(), viewmodel.wrongWordList.value!!.toList(), this)
        dialog.show(parentFragmentManager, "GameResultDialog")
    }

    companion object {
        fun newInstance(): GameFragment {
            val args = Bundle()
            val fragment = GameFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onRetryGame() {
        // 뷰모델에 저장된 5개의 데이터 db에 저장하기
        Log.d("다시하기", "다시하기")
        init()
        startGame()
    }
}
