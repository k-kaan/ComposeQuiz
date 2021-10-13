package at.kcode.guerri.ui.asker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import at.kcode.guerri.ui.theme.GuerriTheme
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

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
                                onDonePressed = { Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show() },
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

