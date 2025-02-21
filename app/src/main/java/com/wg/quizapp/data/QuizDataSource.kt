/*
 * Copyright (c) 2025 QuizApp
 */
package com.wg.quizapp.data

object QuizDataSource {
    fun getQuestions(): List<QuestionType> {
        return listOf(
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
    }
}