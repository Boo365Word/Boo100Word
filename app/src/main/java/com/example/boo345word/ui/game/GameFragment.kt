package com.example.boo345word.ui.game

import android.os.Bundle
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
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.databinding.FragmentGameBinding
import com.example.boo345word.ui.classifier.DrawClassifier
import com.example.boo345word.ui.custom.GameResultDialog
import com.example.boo345word.ui.custom.GameResultDialogListener
import com.example.boo345word.ui.custom.HintDialog
import com.example.boo345word.ui.game.util.GameTimer
import com.example.boo345word.ui.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class GameFragment :
    Fragment(), GameResultDialogListener {

    private val viewModel: GameViewModel by viewModels()
    private var wordList: List<BasicWord> = emptyList()
    private var currentWord: BasicWord? = null
    private lateinit var binding: FragmentGameBinding
    private var stateCount: Int = 5
    private var currentState = 1
    private var falseCount = 0
    private var isPaused: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val classifier = DrawClassifier(requireActivity())

        repeatOnStarted(viewLifecycleOwner) {
            // 여기서 두번째 Job이 생성됨 -> 결과적으로 타이머가 동시에 2번 실행됨
            GameTimer.currentTimer.collect {
                binding.timeProgressBar.progress = it
                if (it == 20) {
                    // 시간이 다 된 경우 넘어가기
                    skipCurrentWord()
                }
            }
        }

        if (currentState == 1) {
            repeatOnStarted(viewLifecycleOwner) {
                viewModel.basicWordList.collect {
                    if (it.isNotEmpty()) {
                        wordList = it
                        startGame()
                    }
                }
            }
        }

        with(binding) {
            btnPause.setOnClickListener {
                isPaused = !isPaused
                if (isPaused) {
                    // 중지버튼 누른 경우 타이머 중지
                    GameTimer.stopTimer()
                    binding.btnPause.setImageResource(R.drawable.btn_play)
                } else {
                    // 다시 한번 누른 경우 타이머 재시작
                    GameTimer.restartTimer()
                    binding.btnPause.setImageResource(R.drawable.btn_pause)
                }
            }
            btnSkip.setOnClickListener {
                skipCurrentWord()
            }

            drawingView.setOnDrawingCompletedListener {
                // 그림을 그릴 때 1 ± 5번째 게임이라면
                if (currentState <= 5) {
                    val newBitmap = binding.drawingView.getBitmap()
                    val result = classifier.classify(newBitmap, currentWord?.word.toString())
                    // 예측 결과가 정답이라면
                    if (result) {
                        lifecycleScope.launch {
                            saveWordResult(true)
                            viewModel.updateState(currentState)
                            binding.ivGhost.setImageResource(R.drawable.img_ghost_v2)
                            binding.btnHint.visibility = View.GONE
                            binding.lottieAnimationView.visibility = View.VISIBLE
                            binding.txtModelSpeech.text = "\"${currentWord?.meaning}\" 정답~!"
                            delay(2000L)
                            currentState++
                            // 정답이라면 다음으로 넘어간다.
                            startGame()
                        }
                    } else {
                        // 오답 횟수에 따른 모델 말 / 사용자 행동 처리
                        falseCount++
                        binding.ivGhost.setImageResource(R.drawable.ghost_v2)
                        when (falseCount) {
                            1 -> binding.txtModelSpeech.text = "다시 그려줄래?"
                            2 -> binding.txtModelSpeech.text = "정답을 입력해봐!"
                            3 -> {
                                binding.txtModelSpeech.text = "힌트를 확인해봐!"
                                binding.btnHint.visibility = View.VISIBLE
                            }
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
                        Toast.makeText(context, "맞았어요!! 힌트를 보고 다시 그려볼까요?", Toast.LENGTH_SHORT)
                            .show()
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

    override fun onRetryGame() {
        // 다시 게임을 하는 경우에도 새로운 랜덤 5개의 단어를 가져온다.
        repeatOnStarted(viewLifecycleOwner) {
            initGameState()
            viewModel.loadData()
            viewModel.basicWordList.collect {
                if (it.isNotEmpty()) {
                    startGame()
                }
            }
        }
    }

    private fun initGameState() {
        // 초기화
        currentState = 1
        viewModel.clearWordList()
        initGameView()
    }

    private fun startGame() {
        // 첫단계인 경우 먼저 데이터를 가져온다.
        // 현재 게임 단계가 5보다 작을 경우 다음 게임을 시작한다.
        if (currentState <= stateCount) {
            // 현재 단계에 맞는 단어를 가져온다.
            updateCurrentWord()
            //  새 게임을 위한 뷰를 준비한다..
            initGameView()
            // 타이머 시작하기
            GameTimer.startTimer()
        } else {
            // 게임이 끝난 경우 결과 다이얼로그를 보여준다.
            showGameResult()
        }
    }

    private fun updateCurrentWord() {
        // 가져온 5개 단어 중 현재 게임 단계에 맞는 단어를 얻는다.
        currentWord = wordList[currentState - 1]
        binding.txtWord.text = currentWord?.word
    }

    private fun initGameView() {
        // 컨버스를 초기화하고, 초기 유령이미지를 보인다.
        falseCount = 0
        with(binding) {
            btnPause.setImageResource(R.drawable.btn_pause)
            drawingView.clearCanvas()
            btnHint.visibility = View.GONE
            lottieAnimationView.visibility = View.GONE
            ivGhost.setImageResource(R.drawable.img_ghost_v2)
            txtModelSpeech.text = "음 ~ 뭐지"
            txtCurrentState.text = String.format(Locale.ENGLISH, "%d", currentState)
        }
    }

    // 스킵 버튼
    private fun skipCurrentWord() {
        // 스킵 시에는 해당 단어를 오답으로 저장한다.
        saveWordResult(false)
        // 다음 게임으로 넘어간다.
        if (currentState < stateCount) {
            currentState++
            viewModel.updateState(currentState)
            GameTimer.stopTimer()
            startGame()
        } else {
            // 만약 현재 단계 >= 5 라면 결과창 보이기
            // 현재 단계가 5인 상태에서 스킵을 누른 경우
            showGameResult()
        }
    }

    // 매 게임마다 게임 결과를 뷰모델에 저장한다.
    private fun saveWordResult(result: Boolean) {
        if (currentWord != null) {
            if (result) {
                viewModel.saveCorrectWord(currentWord!!)
            } else {
                viewModel.saveWrongWord(currentWord!!)
            }
        }
    }

    private fun showGameResult() {
        GameTimer.stopTimer()
        GameResultDialog(
            context = requireContext(),
            correctWordList = viewModel.correctWordList.value.toList(),
            wrongWordList = viewModel.wrongWordList.value.toList(),
            listener = this
        ).show(
            parentFragmentManager,
            "GameResultDialog"
        )
    }

    companion object {

        fun newInstance(): GameFragment {
            val args = Bundle()
            val fragment = GameFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
