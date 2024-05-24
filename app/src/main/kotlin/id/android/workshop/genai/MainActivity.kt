package id.android.workshop.genai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import id.android.workshop.genai.feature.textsummary.TextSummaryViewModel
import id.android.workshop.genai.ui.screen.TextSummaryScreen
import id.android.workshop.genai.ui.theme.GenAIWorkshopTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      GenAIWorkshopTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val textSummaryViewModel: TextSummaryViewModel = viewModel(
            factory = ViewModelFactory
          )
          val textSummaryUiState by textSummaryViewModel.uiState.collectAsState()

          TextSummaryScreen(
            uiState = textSummaryUiState,
            onTextSummaryClick = { inputText, switchState ->
              textSummaryViewModel.summarize(
                inputText,
                switchState
              )
            }
          )
        }
      }
    }
  }
}