package at.kcode.guerri.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
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
                                .padding(16.dp)
                        ) {
                            Text(
                                text = getString(scoreTextByPercentage(), args.rightAnswers, args.totalQuestions),
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp, horizontal = 16.dp)
                            )

                            // todo: local scoreboard - timesstamp - x/y (ranking by percentage)

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { findNavController().navigate(ResultFragmentDirections.goToWelcome()) }) {
                                    Text(text = stringResource(id = R.string.result_button_tryagain))
                                }
                                Button(
                                    onClick = {
                                        // todo: share
                                        Toast.makeText(
                                            context,
                                            getString(
                                                R.string.result_score_share,
                                                getString(R.string.app_name),
                                                args.rightAnswers
                                            ),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }) {
                                    Text(text = stringResource(id = R.string.result_button_share))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @StringRes
    private fun scoreTextByPercentage(): Int {
        val percentage = args.rightAnswers * 100 / args.totalQuestions

        return when {
            percentage == 100 -> R.string.result_score_best
            percentage > 80 -> R.string.result_score_verygood
            percentage > 65 -> R.string.result_score_good
            percentage > 50 -> R.string.result_score_okay
            percentage > 1 -> R.string.result_score_okay
            else -> R.string.result_score_worst
        }
    }
}