package at.kcode.guerri.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import at.kcode.guerri.WelcomeViewModel
import at.kcode.guerri.WelcomeViewState
import at.kcode.guerri.R
import at.kcode.guerri.ui.theme.GuerriTheme

class WelcomeFragment : Fragment() {

    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    //region lifeceycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            welcomeViewModel.loadQuestions(context)
            setContent {
                GuerriTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            WelcomeLogo()
                            WelcomeButton(
                                state = welcomeViewModel.uiEffect().observeAsState(WelcomeViewState.Loading),
                                onRetry = { welcomeViewModel.loadQuestions(context) },
                                onStart = { findNavController().navigate(R.id.asker_fragment) }
                            )
                        }
                    }
                }
            }
        }
    }
    //endregion
}

@Composable
fun WelcomeButton(
    state: State<WelcomeViewState>,
    onRetry: () -> Unit,
    onStart: () -> Unit
) {
    when (state.value) {
        WelcomeViewState.Loading ->
            CircularProgressIndicator()
        WelcomeViewState.Error ->
            OutlinedButton(onClick = onRetry) {
                Text(text = stringResource(R.string.welcome_button_retry))
            }
        WelcomeViewState.Success ->
            OutlinedButton(onClick = onStart) {
                Text(text = stringResource(R.string.welcome_button_start))
            }
    }
}

@Composable
fun WelcomeLogo() {
    Image(
        painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = ""
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val previewButtonState = remember {
        mutableStateOf(WelcomeViewState.Success)
    }
    GuerriTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WelcomeLogo()
                WelcomeButton(
                    state = previewButtonState,
                    onRetry = {},
                    onStart = { }
                )
            }
        }
    }
}