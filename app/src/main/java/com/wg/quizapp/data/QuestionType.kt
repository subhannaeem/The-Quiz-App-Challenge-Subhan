/*
 * Copyright (c) 2025 QuizApp
 */
package com.wg.quizapp.data

sealed class QuestionType {
    data class TrueFalse(val question: String) : QuestionType()
    data class SingleChoice(val question: String, val options: List<String>) : QuestionType()
    data class MultipleChoice(val question: String, val options: List<String>) : QuestionType()
    data class TextInput(val question: String) : QuestionType()
}