package com.wg.quizapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wg.quizapp.data.QuestionType

class QuizViewModel : ViewModel() {

    private val questions = listOf(
        QuestionType.TrueFalse("True or False: An Activity is destroyed during recomposition?"),
        QuestionType.SingleChoice(
            "Which component is used for UI in Jetpack Compose?",
            listOf("View", "Fragment", "Composable", "Activity")
        ),
        QuestionType.MultipleChoice(
            "Which are Android programming languages?",
            listOf("Java", "Kotlin", "Swift", "Dart")
        ),
        QuestionType.TextInput("What is the latest Android version name?"),
        QuestionType.TrueFalse("True or False: Fragments are part of Jetpack Compose?")
    )

    var currentIndex by mutableIntStateOf(0)
        private set

    val currentQuestion: QuestionType?
        get() = if (currentIndex < questions.size) questions[currentIndex] else null

    private val answers = mutableStateMapOf<Int, Any?>()

    fun isCurrentQuestionAnswered(): Boolean {
        return answers.containsKey(currentIndex) && answers[currentIndex] != null &&
                when (currentQuestion) {
                    is QuestionType.MultipleChoice -> (answers[currentIndex] as? Set<*>)?.isNotEmpty() == true
                    is QuestionType.TextInput -> (answers[currentIndex] as? String)?.isNotBlank() == true
                    else -> true
                }
    }

    fun setAnswer(answer: Any) {
        answers[currentIndex] = answer
    }

    fun nextQuestion() {
        if (currentIndex < questions.size) {
            currentIndex++
        }
    }

    fun prevQuestion() {
        if (currentIndex > 0) {
            currentIndex--
        }
    }

    fun isQuizFinished(): Boolean {
        return currentIndex >= questions.size
    }

    fun resetQuiz() {
        currentIndex = 0
        answers.clear()
    }

    fun getSelectedAnswer(): Any? {
        return answers[currentIndex]
    }
}