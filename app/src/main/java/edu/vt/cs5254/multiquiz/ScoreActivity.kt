package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivityScoreBinding

private const val TAG = "ScoreActivity"
private const val EXTRA_SCORE = "edu.vt.cs5254.multiquiz.score"
const val EXTRA_QUIZ_RESET = "edu.vt.cs5254.multiquiz.reset"
class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding
    private val scoreViewModel: ScoreViewModel by viewModels()

    private var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a ScoreViewModel: $scoreViewModel")

        score = intent.getIntExtra(EXTRA_SCORE, 0)

        binding.resetButton.setOnClickListener {
            scoreViewModel.setReset()
            updateView()
        }
        updateView()
    }

    private fun updateView() {
        val scoreText = when {
            scoreViewModel.resetQuiz -> getString(R.string.missing_score)
            else -> score.toString()
        }
        binding.scoreText.text = scoreText
        setQuizReset(scoreViewModel.resetQuiz)

        binding.resetButton.isEnabled = !scoreViewModel.resetQuiz
    }

    private fun setQuizReset(quizIsReset: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_QUIZ_RESET, quizIsReset)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, score: Int): Intent {
            return Intent(packageContext, ScoreActivity::class.java).apply {
                putExtra(EXTRA_SCORE, score)
            }
        }
    }
}