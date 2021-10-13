package at.kcode.guerri.ui.asker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import at.kcode.guerri.ui.theme.GuerriTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskerFragment : Fragment() {

    private val viewModel: AskerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GuerriTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        val questions = viewModel.uiState.observeAsState().value

                        if (questions != null)
                            QuestionsScreen(
                                questions = questions,
                                onDonePressed = {
                                    val totalquestions = questions.questionsState.count()
                                    val rightAnswers = questions.questionsState.count { it.givenAnswerId == it.question.correctAnswerId }
                                    findNavController().navigate(
                                        AskerFragmentDirections.finishQuiz(totalQuestions = totalquestions, rightAnswers = rightAnswers)
                                    )
                                },
                                onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() }
                            )
                        else {
                            Toast.makeText(requireContext(), "An error occurred.", Toast.LENGTH_SHORT).show()
                            activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                    }
                }
            }
        }
    }
}

