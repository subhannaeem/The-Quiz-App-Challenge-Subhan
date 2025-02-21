/*
 * Copyright (c) 2025 QuizApp
 */
import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wg.quizapp.data.QuestionType
import com.wg.quizapp.viewmodels.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val activity = LocalContext.current as? Activity

    if (viewModel.isQuizFinished()) {
        FinishedScreen(viewModel, activity)
    } else {
        val question = viewModel.currentQuestion
        val questionNumber = viewModel.currentIndex + 1
        var isError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (question) {
                is QuestionType.TrueFalse -> TrueFalseQuestion(
                    viewModel, question, questionNumber, isError
                )

                is QuestionType.SingleChoice -> SingleChoiceQuestion(
                    viewModel, question, questionNumber, isError
                )

                is QuestionType.MultipleChoice -> MultipleChoiceQuestion(
                    viewModel, question, questionNumber, isError
                )

                is QuestionType.TextInput -> TextInputQuestion(
                    viewModel, question, questionNumber, isError
                )

                null -> FinishedScreen(viewModel, activity)
            }

            if (isError) {
                Text(
                    text = "Please select an option or enter text to continue!",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.prevQuestion() }, enabled = viewModel.currentIndex > 0
                ) {
                    Text("Back")
                }

                Button(onClick = {
                    if (viewModel.isCurrentQuestionAnswered()) {
                        isError = false
                        viewModel.nextQuestion()
                    } else {
                        isError = true
                    }
                }) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun FinishedScreen(viewModel: QuizViewModel, activity: Activity?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Finished!",
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.resetQuiz() }) {
                Text("Restart")
            }

            Button(
                onClick = { activity?.finish() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Exit")
            }
        }
    }
}

@Composable
fun TrueFalseQuestion(
    viewModel: QuizViewModel,
    question: QuestionType.TrueFalse,
    questionNumber: Int,
    isError: Boolean
) {
    var selected by remember { mutableStateOf(viewModel.getSelectedAnswer() as? Boolean) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Q$questionNumber: ${question.question}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            listOf(true to "True", false to "False").forEach { (value, text) ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selected = value
                            viewModel.setAnswer(value)
                        }
                        .padding(4.dp)) {
                    RadioButton(selected = selected == value, onClick = {
                        selected = value
                        viewModel.setAnswer(value)
                    })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = text)
                }
            }
        }
    }
}

@Composable
fun SingleChoiceQuestion(
    viewModel: QuizViewModel,
    question: QuestionType.SingleChoice,
    questionNumber: Int,
    isError: Boolean
) {
    var selected by remember { mutableStateOf(viewModel.getSelectedAnswer() as? String) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Q$questionNumber: ${question.question}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        question.options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selected = option
                        viewModel.setAnswer(option)
                    }
                    .padding(4.dp)) {
                RadioButton(selected = selected == option, onClick = {
                    selected = option
                    viewModel.setAnswer(option)
                })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestion(
    viewModel: QuizViewModel,
    question: QuestionType.MultipleChoice,
    questionNumber: Int,
    isError: Boolean
) {
    var selected by remember {
        mutableStateOf(
            viewModel.getSelectedAnswer() as? Set<String> ?: emptySet()
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Q$questionNumber: ${question.question}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        question.options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val newSelected =
                            if (selected.contains(option)) selected - option else selected + option
                        selected = newSelected
                        viewModel.setAnswer(newSelected)
                    }
                    .padding(4.dp)) {
                Checkbox(checked = selected.contains(option), onCheckedChange = {
                    val newSelected =
                        if (selected.contains(option)) selected - option else selected + option
                    selected = newSelected
                    viewModel.setAnswer(newSelected)
                })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}

@Composable
fun TextInputQuestion(
    viewModel: QuizViewModel,
    question: QuestionType.TextInput,
    questionNumber: Int,
    isError: Boolean
) {
    var answer by remember { mutableStateOf(viewModel.getSelectedAnswer() as? String ?: "") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Q$questionNumber: ${question.question}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = answer, onValueChange = {
                if (it.length <= 300) {
                    answer = it
                    viewModel.setAnswer(it)
                }
            }, modifier = Modifier.fillMaxWidth()
        )

        if (isError && answer.isEmpty()) {
            Text(
                text = "Please enter text!",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Text(
            text = "${answer.length}/300",
            color = if (answer.length >= 300) Color.Red else Color.Gray,
            modifier = Modifier.align(Alignment.End)
        )
    }
}