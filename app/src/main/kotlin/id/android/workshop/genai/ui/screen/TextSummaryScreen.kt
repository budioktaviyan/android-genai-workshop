package id.android.workshop.genai.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.android.workshop.genai.R
import id.android.workshop.genai.feature.textsummary.TextSummary
import id.android.workshop.genai.feature.textsummary.TextSummary.NORMAL
import id.android.workshop.genai.feature.textsummary.TextSummary.STREAM
import id.android.workshop.genai.feature.textsummary.TextSummaryUiState
import id.android.workshop.genai.ui.theme.GenAIWorkshopTheme

@Composable
fun TextSummaryScreen(
  uiState: TextSummaryUiState = TextSummaryUiState.Loading,
  focusManager: FocusManager = LocalFocusManager.current,
  onTextSummaryClick: (String, TextSummary) -> Unit = { _, _ -> run {} }
) {
  var textToSummarize by rememberSaveable {
    mutableStateOf("")
  }

  Column(
    modifier = Modifier
      .statusBarsPadding()
      .navigationBarsPadding()
      .verticalScroll(rememberScrollState())
  ) {
    ElevatedCard(
      modifier = Modifier
        .padding(
          all = 16.dp
        ).fillMaxWidth(),
      shape = MaterialTheme.shapes.large
    ) {
      OutlinedTextField(
        value = textToSummarize,
        label = { Text(text = stringResource(id = R.string.textsummary_label)) },
        placeholder = { Text(text = stringResource(id = R.string.textsummary_hint)) },
        onValueChange = { input -> textToSummarize = input },
        modifier = Modifier
          .padding(
            all = 16.dp
          ).fillMaxWidth()
      )
      var checked by rememberSaveable {
        mutableStateOf(false)
      }

      Row(
        modifier = Modifier
          .padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
          ).fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Switch(
          checked = checked,
          onCheckedChange = { isChecked ->
            checked = isChecked
          }
        )
        TextButton(
          modifier = Modifier
            .padding(
              start = 4.dp
            ),
          onClick = {
            if (textToSummarize.isNotBlank()) {
              focusManager.clearFocus()

              val switchState = if (checked) STREAM else NORMAL
              onTextSummaryClick(
                textToSummarize,
                switchState
              )
            }
          },
        ) {
          Text(text = stringResource(id = R.string.action_go))
        }
      }
    }

    when (uiState) {
      TextSummaryUiState.Initial -> {}
      TextSummaryUiState.Loading -> {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .padding(
              all = 8.dp
            ).align(Alignment.CenterHorizontally)
        ) {
          CircularProgressIndicator()
        }
      }
      is TextSummaryUiState.Success -> {
        Card(
          modifier = Modifier
            .padding(
              start = 16.dp,
              end = 16.dp,
              bottom = 16.dp
            ).fillMaxWidth(),
          shape = MaterialTheme.shapes.large,
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
          )
        ) {
          Row(
            modifier = Modifier
              .padding(
                all = 16.dp
              ).fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Outlined.Person,
              contentDescription = "Person Icon",
              tint = MaterialTheme.colorScheme.onSecondary,
              modifier = Modifier
                .requiredSize(36.dp)
                .drawBehind {
                  drawCircle(
                    color = Color.White
                  )
                }
            )
            Text(
              text = uiState.output,
              color = MaterialTheme.colorScheme.onSecondary,
              modifier = Modifier
                .padding(
                  start = 16.dp
                ).fillMaxWidth()
            )
          }
        }
      }
      is TextSummaryUiState.Error -> {
        Card(
          modifier = Modifier
            .padding(
              horizontal = 16.dp
            ).fillMaxWidth(),
          shape = MaterialTheme.shapes.large,
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
          )
        ) {
          Text(
            text = uiState.message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
              .padding(
                all = 16.dp
              )
          )
        }
      }
    }
  }
}

@Composable
@Preview(showSystemUi = true)
fun TextSummaryScreenPreview() {
  GenAIWorkshopTheme(darkTheme = true) {
    TextSummaryScreen()
  }
}