/*
 * Copyright (c) 2025 QuizApp
 */
package com.wg.quizapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wg.quizapp.data.QuestionType
import com.wg.quizapp.data.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class QuizViewModel @Inject constructor(private val repository: QuizRepository) : ViewModel() {
    private val questions = repository.getQuestions()

    var currentIndex by mutableIntStateOf(0)
        private set

    val currentQuestion: QuestionType?
        get() = if (currentIndex < questions.size) questions[currentIndex] else null

    private val answers = mutableStateMapOf<Int, Any?>()

    fun isCurrentQuestionAnswered(): Boolean {
        return answers.containsKey(currentIndex) && answers[currentIndex] != null && when (currentQuestion) {
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