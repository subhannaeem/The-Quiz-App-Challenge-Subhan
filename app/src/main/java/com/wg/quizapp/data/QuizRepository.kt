/*
 * Copyright (c) 2025 QuizApp
 */
package com.wg.quizapp.data

class QuizRepository {
    private val questions = QuizDataSource.getQuestions()

    fun getQuestions(): List<QuestionType> = questions
}