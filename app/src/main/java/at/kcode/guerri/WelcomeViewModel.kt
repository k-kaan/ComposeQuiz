package at.kcode.guerri

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.kcode.guerri.data.QuestionRepository
import at.kcode.guerri.data.model.QuestionDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private val uiEffect: MutableLiveData<WelcomeViewState> = MutableLiveData(WelcomeViewState.Loading)
    fun uiEffect(): LiveData<WelcomeViewState> = uiEffect

    fun loadQuestions(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                uiEffect.postValue(WelcomeViewState.Success)
                questionRepository.loadQuestions(context.resources.openRawResource(R.raw.questions))
            } catch (ex: Exception) {
                uiEffect.postValue(WelcomeViewState.Error)
                Timber.e(ex)
            }
        }
    }
}

sealed class WelcomeViewState {
    object Loading : WelcomeViewState()
    object Error : WelcomeViewState()
    object Success : WelcomeViewState()
}