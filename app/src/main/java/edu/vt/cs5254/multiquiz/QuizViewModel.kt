package edu.vt.cs5254.multiquiz

import android.widget.Button
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    private val correctPoints = 25
    private val hintDeduction = 8

    private val questionBank = listOf(
        Question(R.string.question_dp_name, listOf(
            Answer(R.string.question_dp_name_answer_0, true),
            Answer(R.string.question_dp_name_answer_1, false),
            Answer(R.string.question_dp_name_answer_2, false),
            Answer(R.string.question_dp_name_answer_3, false),
        )),
        Question(R.string.question_dp_power, listOf(
            Answer(R.string.question_dp_power_answer_0, false),
            Answer(R.string.question_dp_power_answer_1, true),
            Answer(R.string.question_dp_power_answer_2, false),
            Answer(R.string.question_dp_power_answer_3, false),
        )),
        Question(R.string.question_dp_weapon, listOf(
            Answer(R.string.question_dp_weapon_answer_0, false),
            Answer(R.string.question_dp_weapon_answer_1, false),
            Answer(R.string.question_dp_weapon_answer_2, true),
            Answer(R.string.question_dp_weapon_answer_3, false),
        )),
        Question(R.string.question_dp_break, listOf(
            Answer(R.string.question_dp_break_answer_0, false),
            Answer(R.string.question_dp_break_answer_1, false),
            Answer(R.string.question_dp_break_answer_2, false),
            Answer(R.string.question_dp_break_answer_3, true),
        ))
    )

    private var currentIndex: Int = 0

    private var hintsUsed: Int = 0

    val currentQuestionText: Int get() = questionBank[currentIndex].questionResId
    val currentAnswerChoices: List<Answer> get() = questionBank[currentIndex].answerList
    val onLastQuestion: Boolean get() = currentIndex == questionBank.size - 1

    fun pickAnswer(answerButtons: List<Button>, pickedButton: Button) {
        val pairedAnswers = answerButtons.zip(currentAnswerChoices)
        val pickedAnswer = pairedAnswers.firstOrNull { it.first == pickedButton }?.second ?: return

        pickedAnswer.isSelected = !pickedAnswer.isSelected

        // deselects all other answers
        val otherAnswers = currentAnswerChoices.filter { it != pickedAnswer }
        for (answer in otherAnswers) {
            answer.isSelected = false
        }

    }
    fun useHint() {
        hintsUsed += 1
        val enabledIncorrectAnswers = currentAnswerChoices.filter { !it.isCorrect && it.isEnabled }
        val randomIncorrectAnswer = enabledIncorrectAnswers.random()
        randomIncorrectAnswer.isSelected = false
        randomIncorrectAnswer.isEnabled = false
    }

    fun showFirstQuestion() { currentIndex = 0 }

    fun nextQuestion() { currentIndex = (currentIndex + 1) % questionBank.size }

    fun gradeQuiz(): Int {
        val totalCorrect = questionBank.count { it.answerList.any { it.isSelected && it.isCorrect } }
        return (totalCorrect * correctPoints) - (hintsUsed * hintDeduction)
    }
    
    fun resetQuiz() {
        currentIndex = 0
        hintsUsed = 0
        for (question in questionBank) {
            for (answer in question.answerList) {
                answer.isSelected = false
                answer.isEnabled = true
            }
        }
    }
}