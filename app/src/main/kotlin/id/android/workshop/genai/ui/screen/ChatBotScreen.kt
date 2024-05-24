package id.android.workshop.genai.ui.screen

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import id.android.workshop.genai.R
import id.android.workshop.genai.feature.chatbot.ChatBotMessage
import id.android.workshop.genai.feature.chatbot.Participant.ERROR
import id.android.workshop.genai.feature.chatbot.Participant.MODEL
import id.android.workshop.genai.feature.chatbot.Participant.USER
import kotlinx.coroutines.launch

@Composable
fun ChatList(
  chatBotMessages: List<ChatBotMessage>,
  listState: LazyListState
) {
  LazyColumn(
    reverseLayout = true,
    state = listState
  ) {
    items(
      items = chatBotMessages.reversed()
    ) { message ->
      ChatBubbleItem(chatBotMessage = message)
    }
  }
}

@Composable
fun ChatBubbleItem(
  chatBotMessage: ChatBotMessage
) {
  val isModelMessage =
    chatBotMessage.participant == MODEL ||
    chatBotMessage.participant == ERROR

  val backgroundColor = when (chatBotMessage.participant) {
    MODEL -> { MaterialTheme.colorScheme.primaryContainer }
    USER -> { MaterialTheme.colorScheme.tertiaryContainer }
    ERROR -> { MaterialTheme.colorScheme.errorContainer }
  }

  val bubbleShape = if (isModelMessage) {
    RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
  } else {
    RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
  }

  val horizontalAlignment = if (isModelMessage) {
    Alignment.Start
  } else {
    Alignment.End
  }

  Column(
    horizontalAlignment = horizontalAlignment,
    modifier = Modifier
      .padding(
        horizontal = 8.dp,
        vertical = 4.dp
      ).fillMaxWidth()
  ) {
    Text(
      text = chatBotMessage.participant.name,
      style = MaterialTheme.typography.bodySmall,
      modifier = Modifier
        .padding(
          bottom = 4.dp
        )
    )
    Row {
      if (chatBotMessage.isPending) {
        CircularProgressIndicator(
          modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(
              all = 8.dp
            )
        )
      }

      BoxWithConstraints {
        Card(
          colors = CardDefaults.cardColors(containerColor = backgroundColor),
          shape = bubbleShape,
          modifier = Modifier
            .widthIn(
              min = 0.dp,
              max = maxWidth.times(0.9f)
            )
        ) {
          Text(
            text = chatBotMessage.text,
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
fun MessageInput(
  onSendMessage: (String) -> Unit,
  resetScroll: () -> Unit = {}
) {
  var userMessage by rememberSaveable {
    mutableStateOf("")
  }

  val scrollState = rememberScrollState()
  val coroutineScope = rememberCoroutineScope()
  val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

  LaunchedEffect(key1 = keyboardHeight) {
    coroutineScope.launch {
      scrollState.scrollBy(keyboardHeight.toFloat())
    }
  }

  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .verticalScroll(scrollState)
        .windowInsetsPadding(WindowInsets.safeContent.only(WindowInsetsSides.Bottom))
        .padding(
          all = 16.dp
        ).fillMaxWidth()
    ) {
      OutlinedTextField(
        value = userMessage,
        label = { Text(text = stringResource(id = R.string.chatbot_label)) },
        onValueChange = { message -> userMessage = message },
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Sentences
        ),
        modifier = Modifier
          .align(Alignment.CenterVertically)
          .fillMaxWidth()
          .weight(0.85f)
      )
      IconButton(
        onClick = {
          if (userMessage.isNotBlank()) {
            onSendMessage(userMessage)
            userMessage = ""
            resetScroll()
          }
        },
        modifier = Modifier
          .padding(
            start = 16.dp
          )
          .align(Alignment.CenterVertically)
          .fillMaxWidth()
          .weight(0.15f)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Send,
          contentDescription = stringResource(id = R.string.chatbot_send),
          modifier = Modifier
        )
      }
    }
  }
}