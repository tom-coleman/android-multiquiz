package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel()  {

    var resetQuiz: Boolean = false

    fun setReset() { resetQuiz = true }
}