package at.kcode.guerri.ui.asker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import at.kcode.guerri.R
import at.kcode.guerri.data.model.AnswerDTO
import at.kcode.guerri.data.model.QuestionDTO
import at.kcode.guerri.supportWideScreen

@Composable
fun QuestionsScreen(
    questions: Questions,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit
) {
    val questionState = remember(questions.currentQuestionIndex) {
        questions.questionsState[questions.currentQuestionIndex]
    }

    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {
                QuizTopAppBar(
                    questionIndex = questionState.questionIndex,
                    totalQuestionsCount = questionState.totalQuestions,
                    onBackPressed = onBackPressed
                )
            },
            content = { innerPadding ->
                QuestionContent(
                    question = questionState.question,
                    selectedAnswer = questionState.givenAnswerId,
                    onAnswer = {
                        questionState.givenAnswerId = it.id
                        questionState.enableNext = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            },
            bottomBar = {
                NavigationButtons(
                    questionState = questionState,
                    onPreviousPressed = { questions.currentQuestionIndex-- },
                    onNextPressed = { questions.currentQuestionIndex++ },
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}

//region content
@Composable
private fun QuestionContent(
    question: QuestionDTO,
    selectedAnswer: String,
    onAnswer: (AnswerDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            QuestionTitle(question.question)
            Spacer(modifier = Modifier.height(24.dp))

            SingleChoiceQuestion(
                question = question,
                selectedAnswer = selectedAnswer,
                onAnswerSelected = { answer -> onAnswer(answer) },
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
private fun QuestionTitle(title: String) {
    val backgroundColor = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun SingleChoiceQuestion(
    question: QuestionDTO,
    selectedAnswer: String = "",
    onAnswerSelected: (AnswerDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = question.answers

    val (selectedOption, onOptionSelected) = remember(selectedAnswer) {
        mutableStateOf(selectedAnswer)
    }

    Column(modifier = modifier) {
        radioOptions.forEach { answer ->
            val onClickHandle = {
                onOptionSelected(answer.id)
                onAnswerSelected(answer)
            }

            val optionSelected = answer.id == selectedOption

            val answerBorderColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }
            val answerBackgroundColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionSelected,
                            onClick = onClickHandle
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = answer.text
                    )

                    RadioButton(
                        selected = optionSelected,
                        onClick = onClickHandle,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}
//endregion

//region header&footer
@Composable
private fun QuizTopAppBar(questionIndex: Int, totalQuestionsCount: Int, onBackPressed: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TopAppBarTitle(
                questionIndex = questionIndex,
                totalQuestionsCount = totalQuestionsCount,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .align(Alignment.Center)
            )

            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.asker_cancel_description),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }

        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            backgroundColor = if (!isSystemInDarkTheme()) Color.Black.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.24f)
        )
    }
}

@Composable
private fun TopAppBarTitle(questionIndex: Int, totalQuestionsCount: Int, modifier: Modifier = Modifier) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val totalStyle = MaterialTheme.typography.caption.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${questionIndex + 1} ")
        }
        withStyle(style = totalStyle) {
            append(stringResource(R.string.asker_title_totalquestions, totalQuestionsCount))
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}

@Composable
private fun NavigationButtons(questionState: QuestionState, onPreviousPressed: () -> Unit, onNextPressed: () -> Unit, onDonePressed: () -> Unit) {
    Surface(
        elevation = 7.dp,
        modifier = Modifier.fillMaxWidth() // .border(1.dp, MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (questionState.showPrevious) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.asker_nav_back))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = questionState.enableNext,
                onClick = if (questionState.showDone)
                    onDonePressed
                else
                    onNextPressed
            ) {
                if (questionState.showDone)
                    Text(text = stringResource(id = R.string.asker_nav_finish))
                else
                    Text(text = stringResource(id = R.string.asker_nav_next))
            }
        }
    }
}
//endregion