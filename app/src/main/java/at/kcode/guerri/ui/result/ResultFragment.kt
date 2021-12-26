package at.kcode.guerri.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.kcode.guerri.R
import at.kcode.guerri.ui.theme.GuerriTheme

class ResultFragment : Fragment() {

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GuerriTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(Dp(16f))
                        ) {
                            Text(
                                text = "score",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp, horizontal = 16.dp)
                            )
                            // "Score" klein
                            // score text groß mit emoji
                            // x/y klein

                            // scoreboard

                            // unten horizontal: share, retry




                            Text(text = "${args.rightAnswers}")
                            Text(text = "${args.totalQuestions}")
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = { findNavController().navigate(ResultFragmentDirections.goToWelcome()) }) {
                                Text(text = stringResource(id = R.string.result_button_tryagain))
                            }
                        }
                    }
                }
            }
        }
    }
}