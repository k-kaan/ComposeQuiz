package at.kcode.guerri.ui.asker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.kcode.guerri.data.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AskerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<Questions>()
    val uiState: LiveData<Questions> = _uiState

    init {
        viewModelScope.launch {
            val questions = questionRepository.question.receive()

            val questionStates = questions.mapIndexed { index, question ->
                val showPrevious = index > 0
                val showDone = index == questions.size - 1

                QuestionState(
                    question = question,
                    questionIndex = index,
                    totalQuestions = questions.size,
                    showPrevious = showPrevious,
                    showDone = showDone
                )
            }

            _uiState.value = Questions(questionStates)
        }
    }

}