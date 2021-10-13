package at.kcode.guerri.data.model

data class QuestionsDTO(
    val questions: List<QuestionDTO>
)

data class QuestionDTO(
    val question: String,
    val answers: List<AnswerDTO>,
    val correctAnswerId: String
)

data class AnswerDTO(
    val id: String,
    val text: String
)