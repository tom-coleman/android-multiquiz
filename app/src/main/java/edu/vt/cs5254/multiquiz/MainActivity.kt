package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    // Name: Tom Coleman
    // Username: tomcoleman

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var answerButtons: List<Button>

    private val scoreLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == RESULT_OK) {
            if (result.data?.getBooleanExtra(EXTRA_QUIZ_RESET, false) == true) {
                quizViewModel.resetQuiz()
            } else {
                quizViewModel.showFirstQuestion()
            }
            updateView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        // Zip the buttons with the question answer list?
        answerButtons = listOf(
            binding.answer0Button,
            binding.answer1Button,
            binding.answer2Button,
            binding.answer3Button
        )

        for (button in answerButtons) {
            button.setOnClickListener { view: View ->
                quizViewModel.pickAnswer(answerButtons, button)
                updateView()
            }
        }

        binding.hintButton.setOnClickListener { view: View ->
            quizViewModel.useHint()
            updateView()
        }

        binding.submitButton.setOnClickListener { view: View ->
            if (quizViewModel.onLastQuestion) {
                val finalScore = quizViewModel.gradeQuiz()
                val intent = ScoreActivity.newIntent(this@MainActivity, finalScore)
                scoreLauncher.launch(intent)
            } else {
                quizViewModel.nextQuestion()
                updateView()
            }
        }

        updateView()
    }

    private fun updateView() {
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
        val pairedAnswers = answerButtons.zip(quizViewModel.currentAnswerChoices)

        // Update the answer buttons
        for (pair in pairedAnswers) {
            pair.first.isEnabled = pair.second.isEnabled
            pair.first.isSelected = pair.second.isSelected
            pair.first.setText(pair.second.textResId)
            pair.first.updateColor()
        }

        // Enable/disable the hint and submit buttons
        binding.hintButton.isEnabled = quizViewModel.currentAnswerChoices.any { it.isEnabled && !it.isCorrect }
        binding.submitButton.isEnabled = quizViewModel.currentAnswerChoices.any { it.isSelected }
    }
}